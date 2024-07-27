package com.ykdz.ThreadLocal.thread.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.*;

@Slf4j
public class FuturesTask {
    public void convertRunnableToCallable() throws ExecutionException,InterruptedException{
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
              log.info("inside callable future task ...");
              return 0;
            }
        });
        Thread thread= new Thread(futureTask);
        thread.start();
        log.info(futureTask.get().toString());
    }
}
