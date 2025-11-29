package com.jonah.aspect.exception;

import com.jonah.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    private static final ThreadLocal<Boolean> exceptionLogged =
            ThreadLocal.withInitial(() -> false);

    /**
     * Handles exceptions and logs them appropriately
     * Business exceptions (ResourceNotFoundException, IllegalArgumentException) - WARN
     * Unexpected exceptions - ERROR
     */
    @Around("execution(* com.jonah.service..*(..)) || execution(* com.jonah.controller..*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            // Prevent duplicate logging (logging aspect might also log)
            if (!exceptionLogged.get()) {
                logException(e, className, methodName, duration);
                exceptionLogged.set(true);
            }

            throw e;  // Re-throw for GlobalExceptionHandler
        } finally {
            exceptionLogged.remove();
        }
    }

    private void logException(Exception e, String className, String methodName, long duration) {
        switch (e) {
            case ResourceNotFoundException ex ->
                    log.warn("Resource not found in {}.{}() | Duration: {}ms | Reason: {}",
                            className, methodName, duration, ex.getMessage());

            case IllegalArgumentException ex ->
                    log.warn("Invalid input in {}.{}() | Duration: {}ms | Reason: {}",
                            className, methodName, duration, ex.getMessage());

            default ->
                    log.error("Unexpected error in {}.{}() | Duration: {}ms | Error: {}",
                            className, methodName, duration, e.getMessage(), e);
        }
    }
}
