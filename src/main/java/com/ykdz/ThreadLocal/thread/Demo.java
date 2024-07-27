package com.ykdz.ThreadLocal.thread;

import java.util.concurrent.*;

public class Demo {
    public static class MyThread extends Thread {
        @Override
        public void run(){
            System.out.println("MyThread");
        }

        public static void main(String[] args) {
            MyThread myThread =  new MyThread();
            myThread.start();
        }
    }


    public static class Task implements Callable<Integer>{

      @Override
      public Integer call() throws Exception {
          Thread.sleep(1000);
          return 2;
      }

      public static void main(String[] args) throws ExecutionException, InterruptedException {
          ExecutorService executorService = Executors.newCachedThreadPool();
          Task task = new Task();
          Future<Integer> result = executorService.submit(task);
          System.out.println(result.get());
      }
  }




}
