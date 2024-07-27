package com.ykdz.aop.service;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect {

    @Pointcut("execution(* com.ykdz.aop.service.*.*(..))")
    public void myPointcut(){

    }

    @Before("execution(* com.ykdz.aop.biz.*.*(..))")
    public void before(){
        System.out.println("before");
    }
    @AfterReturning("execution(* com.ykdz.aop.biz.*.*(..))")
    public void after(){
        System.out.println("after");
    }
}
