package com.ykdz.ThreadLocal.completableFuture.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTest3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        //可以自定义线程池
//        ExecutorService executor = Executors.newCachedThreadPool();
//        //runAsync的使用
//        CompletableFuture<Void> runFuture = CompletableFuture.runAsync(() -> System.out.println("run,关注公众号:捡田螺的小男孩"), executor);
//        //supplyAsync的使用
//        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
//            System.out.print("supply,关注公众号:捡田螺的小男孩");
//            return "捡田螺的小男孩"; }, executor);
//        //runAsync的future没有返回值，输出null
//        System.out.println(runFuture.join());
//        //supplyAsync的future，有返回值
//        System.out.println(supplyFuture.join());
//        executor.shutdown(); // 线程池需要关闭
        testAllOf();
    }

    public static void thenRun() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> System.out.println("这是第一次异步工作"));
        CompletableFuture<Void> completableFuture = runAsync.thenRunAsync(() -> System.out.println("这是第二次异步工作"), executor);
        System.out.println(completableFuture.get());
    }

    public static void thenAccept() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture<String> runAsync = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("这是初始的异步工作");
                    return "我的名字是小明";
                });
        CompletableFuture<String> completableFuture = runAsync.thenApply(
                (a) -> {
                    if("我的名字是小明".equals(a)){
                        return "关注了";
                    }
                    return "先考虑下";
                });
        System.out.println(completableFuture.get());
    }

    public static void whenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> orgFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程名称为"+Thread.currentThread().getName());
            try{
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "我是小明，我已完成任务";
        });

        CompletableFuture<String> rstFuture = orgFuture.whenComplete((a, throwable) -> {
            System.out.println("当前线程名称为"+Thread.currentThread().getName());
            System.out.println("上个任务执行完成，还把"+a+"传过来");
            if ("我是小明，我已完成任务".equals(a)){
                System.out.println("安全到达总部");
            }else {
                System.out.println("途中遭遇不测");
            }
        });
        System.out.println(rstFuture.get());
    }

    public static void thenComBineTest(){
        CompletableFuture<String> first = CompletableFuture.completedFuture("第一个异步任务");
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "第二个异步任务", executorService).thenCombineAsync(first, (s, w) -> {
            System.out.println(w);
            System.out.println(s);
            return "两个异步任务的组合";
        }, executorService);
        //阻塞主线程进行异步结果的输出
        System.out.println(future.join());
        //关闭线程池
        executorService.shutdown();

    }

    public static void testAllOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> "第一个任务执行完成");
        CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> "第二个任务执行完成");
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("出现异常");
        });

        CompletableFuture<Void> future = CompletableFuture.allOf(a, b).whenComplete((m, throwable) -> {
            System.out.println("finish");
            System.out.println("........");
        });
        System.out.println(future.get());
    }

}
