package com.ykdz.aop;

import com.ykdz.aop.annotation.Aspect;
import com.ykdz.aop.annotation.Before;
import com.ykdz.aop.annotation.PointCut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAdvice {
   //定义一个切点
    @PointCut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void logAdvicePointcut(){}

    @Before("logAdvicePointcut()")
    public void logAdvice(){
        // 这里只是一个示例，你可以写任何处理逻辑
        System.out.println("get请求的advice触发了");
    }



}
