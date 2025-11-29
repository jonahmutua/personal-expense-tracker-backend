package com.jonah.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PerformanceAspect {

    private static final long SLOW_THRESHOLD_MS = 500;

    @Pointcut("@annotation(com.jonah.annotation.Log) || @within(com.jonah.annotation.Log)")
    public void loggedMethods() {}

    @Around("loggedMethods()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (duration > SLOW_THRESHOLD_MS) {
                log.warn("SLOW: {}.{}() | Execution time {} ms",
                        className, methodName, duration);
            } else {
                log.debug("{}.{}() | Execution time: {} ms",
                        className, methodName, duration);
            }
        }
    }
}
