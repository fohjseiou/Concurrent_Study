package com.thread.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ScheduleService {
    //开启5个线程同时进行处理
    ExecutorService service = Executors.newFixedThreadPool(5);
    @Scheduled(cron = "0/1 * * * * ? ")
    public void deleteFile() throws InterruptedException {
        service.execute(()->{
            try {
                log.info("111delete success, time:" + new Date().toString());
                Thread.sleep(1000 * 5);//模拟长时间执行，比如IO操作，http请求
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        //释放
        service.shutdown();

    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void syncFile() {
        log.info("222sync success, time:" + new Date().toString());
    }

}
