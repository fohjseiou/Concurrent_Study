package com.ykdz.ThreadLocal.completableFuture.controller;


import com.ykdz.ThreadLocal.completableFuture.entity.MedalInfo;
import com.ykdz.ThreadLocal.completableFuture.entity.UserInfo;
import com.ykdz.ThreadLocal.completableFuture.service.MedalService;
import com.ykdz.ThreadLocal.completableFuture.service.UserInfoService;

import java.util.concurrent.*;


public class FutureTest2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Integer userId = 666;
        UserInfoService userInfoService = new UserInfoService();
        MedalService medalService = new MedalService();

        //系统当前时间
        long start = System.currentTimeMillis();
            //系统需要哪些
            CompletableFuture<UserInfo> userInfoCompletableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return userInfoService.getUserInfo(userId);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService);

            Thread.sleep(300); //模拟主线程其它操作耗时

            CompletableFuture<MedalInfo> medalInfoCompletableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return medalService.getMedalInfo(userId);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService);

        UserInfo userInfo = userInfoCompletableFuture.get(2, TimeUnit.SECONDS);
        MedalInfo medalInfo = medalInfoCompletableFuture.get();
        System.out.println(userInfo);
        System.out.println(medalInfo);
        //耗时
        System.out.println("耗时：" + (System.currentTimeMillis() - start));

    }
}
