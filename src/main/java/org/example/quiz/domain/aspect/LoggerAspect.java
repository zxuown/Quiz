package org.example.quiz.domain.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Before(value = "org.example.quiz.domain.aspect.AspectPointCut.loggableMethods()")
    public void logInfoBeforeLoggable(JoinPoint joinPoint) {
        log.info("BEFORE loggableMethods() \n{}\n{}", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature());
    }

    @After("org.example.quiz.domain.aspect.AspectPointCut.loggableMethods()")
    public void logInfoAfterLoggable(JoinPoint joinPoint) {
        log.info("AFTER loggableMethods() \n{}\n{}", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature());
    }
}
