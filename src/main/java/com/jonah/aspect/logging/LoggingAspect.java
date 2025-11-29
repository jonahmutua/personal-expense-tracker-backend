package com.jonah.aspect.logging;

import com.jonah.aspect.annotation.Log;
import com.jonah.exception.ResourceNotFoundException;
import com.jonah.utils.LoggingUtil;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut 1: Methods directly annotated with @Log
     */
    @Pointcut("@annotation(com.jonah.annotation.Log)")
    public void methodLevelLog() {}

    /**
     * Pointcut 2: Methods in classes annotated with @Log
     */
    @Pointcut("@target(com.jonah.annotation.Log)")
    public void classLevelLog() {}

    /**
     * Combined: Match either method or class level
     */
    @Around("methodLevelLog() || classLevelLog()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        // Check if logging is enabled (method level can disable it)
        if (!isLoggingEnabled(joinPoint)) {
            return joinPoint.proceed();
        }

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("Entering {}.{}() | Args: {}",
                className, methodName,
                Arrays.stream(args)
                        .map(LoggingUtil::safeToLog)
                        .toArray());

        Object result = joinPoint.proceed();

        log.info("Exiting {}.{}() | Result: {}",
                className, methodName, LoggingUtil.safeToLog(result));

        return result;
    }

    /**
     * Check if logging is enabled
     * Method level @Log(enabled=false) can disable logging for that method
     */
    private boolean isLoggingEnabled(ProceedingJoinPoint joinPoint) {
        try {
            // Check method level annotation first (has priority)
            Log methodAnnotation = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName())
                    .getAnnotation(Log.class);

            if (methodAnnotation != null) {
                return methodAnnotation.enabled();
            }

            // Fall back to class level annotation
            Log classAnnotation = joinPoint.getTarget().getClass()
                    .getAnnotation(Log.class);

            if (classAnnotation != null) {
                return classAnnotation.enabled();
            }

        } catch (NoSuchMethodException e) {
            // Method not found, skip logging
        }

        return true;  // Default: enable logging
    }
}

