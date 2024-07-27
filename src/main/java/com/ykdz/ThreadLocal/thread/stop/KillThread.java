package com.ykdz.ThreadLocal.thread.stop;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KillThread implements Runnable{
    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private int interval;

    public KillThread(int sleepInterval) {
        interval = sleepInterval;
    }
    private void start(){
       worker = new Thread(this);
       worker.start();
    }

    private void stop(){
       running.set(false);
    }

    @Override
    public void run() {running.set(true);
      while (running.get()){
          try {
              log.info("线程开始咯！！！");
              Thread.sleep(interval);
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              log.info("Thread was interrupted, Failed to complete operation");
          }
          log.info("finished");
      }
    }

    public static void main(String[] args) {
        KillThread killThread= new KillThread(1000);
        killThread.start();
        killThread.stop();
    }
}
