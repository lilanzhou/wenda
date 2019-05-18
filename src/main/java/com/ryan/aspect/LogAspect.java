package com.ryan.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by Administrator on 2019:04:26
 *@Author : Lilanzhou
 *功能 :
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    Date date = new Date();
    @Before("execution(* com.ryan.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for (Object arg:joinPoint.getArgs()
                ) {
            if (arg != null) {
                sb.append("arg:" + arg.toString() + "|");
            }
        }

        date.getTime();
        logger.info("before method:"+sb.toString()+date.getTime());
    }

    @After("execution(* com.ryan.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){

        logger.info("after method:"+ date.getTime());
    }
}
