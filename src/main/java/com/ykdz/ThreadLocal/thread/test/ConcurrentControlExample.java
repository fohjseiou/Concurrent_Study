package com.ykdz.ThreadLocal.thread.test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ConcurrentControlExample {

        public static void main(String[] args) throws InterruptedException {
            int maxConcurrency = 1000;
            int totalTasks = 1000; // 总共要执行的任务数量
            ExecutorService executor = Executors.newFixedThreadPool(maxConcurrency);
            Semaphore semaphore = new Semaphore(maxConcurrency); // 控制并发量
            CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

            // 提交任务
            IntStream.range(0, totalTasks).forEach(i -> {
                Runnable task = () -> {
                    try {
                        semaphore.acquire(); // 获取许可
                        try {
                            // 模拟耗时操作
                            Thread.sleep(2000);
                            System.out.println("Task " + i + " completed.");
                        } finally {
                            semaphore.release(); // 释放许可
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                };

                // 将任务包装为 Callable，以便可以获取 Future
                Callable<Void> callable = () -> {
                    task.run();
                    return null;
                };

                completionService.submit(callable);
            });

            // 等待所有任务完成，同时处理超时
            for (int i = 0; i < totalTasks; i++) {
                try {
                    Future<Void> future = completionService.take();
                    future.get(1, TimeUnit.SECONDS); // 尝试获取结果，等待1秒
                } catch (ExecutionException e) {
                    System.err.println("Error executing task: " + e.getCause());
                } catch (TimeoutException e) {
                    System.out.println("Task took longer than 1 second.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread was interrupted while waiting for a task to complete.");
                }
            }

            executor.shutdown();
        }
}
