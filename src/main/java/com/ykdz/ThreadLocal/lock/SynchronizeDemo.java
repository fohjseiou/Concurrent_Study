package com.ykdz.ThreadLocal.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SynchronizeDemo implements Runnable{
    @Override
    public void run() {
        test2();
    }

    private void test1(){
        System.out.println(Thread.currentThread().getName() + "_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        synchronized (this){
            try {
                System.out.println(Thread.currentThread().getName() + "_start_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "_end_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private synchronized static void test2(){
        System.out.println(Thread.currentThread().getName() + "_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));

            try {
                System.out.println(Thread.currentThread().getName() + "_start_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "_end_: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }catch (Exception e){
                e.printStackTrace();
            }

    }

    public static void main(String[] args) {
        SynchronizeDemo synchronizeDemo = new SynchronizeDemo();
        Thread thread1 = new Thread(synchronizeDemo,"thread1");
        Thread thread2 = new Thread(synchronizeDemo,"thread2");
        Thread thread3 = new Thread(new SynchronizeDemo(),"thread3");
        Thread thread4 = new Thread(new SynchronizeDemo(),"thread4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
