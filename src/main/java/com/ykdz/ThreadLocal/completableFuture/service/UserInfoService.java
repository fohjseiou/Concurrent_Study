package com.ykdz.ThreadLocal.completableFuture.service;

import com.ykdz.ThreadLocal.completableFuture.entity.UserInfo;
import org.springframework.stereotype.Service;

public class UserInfoService {
    public UserInfo getUserInfo(Integer userId) throws InterruptedException {
        Thread.sleep(300);
        return new UserInfo(userId,"李小小",22);
    }
}
