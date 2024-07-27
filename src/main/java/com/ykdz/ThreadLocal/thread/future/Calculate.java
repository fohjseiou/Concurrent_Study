package com.ykdz.ThreadLocal.thread.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Calculate {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Future<Integer> futureOne = calculate(10);
        Future<Integer> futureTwo = calculate(20);
        while (!(futureOne.isDone() && futureTwo.isDone())){
            System.out.println(
                    String.format(
                            "future1 is %s and future2 is %s",
                            futureOne.isDone() ? "done" : "not done",
                            futureTwo.isDone() ? "done" : "not done"
                    )
            );
            Thread.sleep(3000);
        }
        Integer result1 = futureOne.get();
        Integer result2 = futureTwo.get();

        System.out.println(result1 + " and " + result2);

    }

    public static Future<Integer> calculate(Integer input){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        return executor.submit(()->{
            try {
                System.out.println("Calculating..."+input);
                Thread.sleep(1000);
                return input * input;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
