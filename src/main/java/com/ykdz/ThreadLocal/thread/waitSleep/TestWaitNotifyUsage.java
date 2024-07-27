package com.ykdz.ThreadLocal.thread.waitSleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestWaitNotifyUsage {

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyUsage waitNotifyUsage = new WaitNotifyUsage();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(()->{
            try {
                waitNotifyUsage.produceMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.submit(()->{
            try {
                waitNotifyUsage.consumeMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(50000);
    }
}
