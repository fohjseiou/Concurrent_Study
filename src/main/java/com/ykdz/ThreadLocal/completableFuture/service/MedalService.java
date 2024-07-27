package com.ykdz.ThreadLocal.completableFuture.service;

import com.ykdz.ThreadLocal.completableFuture.entity.MedalInfo;
import com.ykdz.ThreadLocal.completableFuture.entity.UserInfo;

public class MedalService {
    public MedalInfo getMedalInfo(Integer userId) throws InterruptedException {
        //模拟系统耗时
        Thread.sleep(500);
        return new MedalInfo(userId, "金牌");
    }
}
