package org.example.quiz.domain.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class AspectPointCut {
    @Pointcut("execution(* (@Loggable *).*(..))")public void loggableMethods() {}
}
