package com.ykdz.aop.controller;

import com.ykdz.aop.annotation.Aspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
@Slf4j
public class PermissionFirstAdvice  {


}
