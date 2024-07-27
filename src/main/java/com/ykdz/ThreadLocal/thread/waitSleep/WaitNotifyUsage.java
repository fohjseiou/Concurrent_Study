package com.ykdz.ThreadLocal.thread.waitSleep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitNotifyUsage {
    private int count = 0;

    public void produceMessage() throws InterruptedException {
        while (true){
           synchronized (this){
               while (count == 5){
                   log.info("count == 5 , wait ....");
                   wait();
               }
               count++;
               log.info(Thread.currentThread().getName()+"produce count {}", count);
               notify();
           }
        }
    }


    public void consumeMessage() throws InterruptedException {
        while (true){
            synchronized (this){
                while (count == 0){
                    log.info("count == 0 , wait ....");
                    wait();
                }
                count--;
                log.info(Thread.currentThread().getName()+"produce count {}", count);
                notify();
            }
        }
    }
}
