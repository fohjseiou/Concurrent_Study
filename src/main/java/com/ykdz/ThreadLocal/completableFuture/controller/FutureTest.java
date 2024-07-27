package com.ykdz.ThreadLocal.completableFuture.controller;


import com.ykdz.ThreadLocal.completableFuture.entity.MedalInfo;
import com.ykdz.ThreadLocal.completableFuture.entity.UserInfo;
import com.ykdz.ThreadLocal.completableFuture.service.MedalService;
import com.ykdz.ThreadLocal.completableFuture.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


public class FutureTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Integer userId = 666;
        UserInfoService userInfoService = new UserInfoService();
        MedalService medalService = new MedalService();

        //系统当前时间
        long start = System.currentTimeMillis();
        //系统需要哪些
        FutureTask<UserInfo> userInfoFutureTask = new FutureTask<>(new Callable<UserInfo>() {
            @Override
            public UserInfo call() throws Exception {
                return userInfoService.getUserInfo(userId);
            }
        });
        executorService.submit(userInfoFutureTask);

        Thread.sleep(300); //模拟主线程其它操作耗时

        FutureTask<MedalInfo> medalInfoFutureTask = new FutureTask<>(new Callable<MedalInfo>() {
            @Override
            public MedalInfo call() throws Exception {
                return medalService.getMedalInfo(userId);
            }
        });
        executorService.submit(medalInfoFutureTask);
        executorService.shutdown();
        UserInfo userInfo = userInfoFutureTask.get();//获取个人信息结果
        MedalInfo medalInfo = medalInfoFutureTask.get();//获取勋章信息结果
        System.out.println(userInfo);
        System.out.println(medalInfo);
        //耗时
        System.out.println("耗时：" + (System.currentTimeMillis() - start));

    }
}
