package com.jonah.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PerformanceAspect {

    private static final long SLOW_THRESHOLD_MS = 500;
    private ProceedingJoinPoint joinPoint;

    /**
     * Monitors execution time and logs slow operations
     */
    @Around("execution(* com.jonah.service..*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        this.joinPoint = joinPoint;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (duration > SLOW_THRESHOLD_MS) {
                log.warn("SLOW: {}.{}() | execution time: {} ms",
                        className, methodName, duration);
            } else {
                log.info("{}.{}() | execution time: {} ms",
                        className, methodName, duration);
            }
        }
    }
}
