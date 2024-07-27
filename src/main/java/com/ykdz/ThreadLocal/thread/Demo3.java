package com.ykdz.ThreadLocal.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Demo3 {
    public static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"执行任务");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+"执行完毕");
        }
    }


    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Task(), 1, TimeUnit.SECONDS);
    }

}
