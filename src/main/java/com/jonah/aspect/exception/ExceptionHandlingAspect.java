package com.jonah.aspect.exception;

import com.jonah.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    private static final ThreadLocal<Boolean> exceptionLogged =
            ThreadLocal.withInitial(() -> false);

    @Pointcut("@annotation(com.jonah.aspect.annotation.Log) || @within(com.jonah.aspect.annotation.Log)")
    public void loggedMethods() {
    }

    @Around("loggedMethods()")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            if (!exceptionLogged.get()) {
                logException(e, className, methodName, duration);
                exceptionLogged.set(true);
            }

            throw e;
        } finally {
            exceptionLogged.remove();
        }
    }

    private void logException(Exception e, String className, String methodName, long duration) {
        switch (e) {
            case ResourceNotFoundException ex ->
                    log.warn("Resource not found in {}.{}() | Execution time: {}ms | Reason: {}",
                            className, methodName, duration, ex.getMessage());

            case IllegalArgumentException ex -> log.warn("Invalid input in {}.{}() | Execution time: {}ms | Reason: {}",
                    className, methodName, duration, ex.getMessage());

            default -> log.error("Unexpected error in {}.{}() | Execution time: {}ms | Error: {}",
                    className, methodName, duration, e.getMessage(), e);
        }
    }
}
