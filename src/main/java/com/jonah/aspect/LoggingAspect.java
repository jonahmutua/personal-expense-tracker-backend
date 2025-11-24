package com.jonah.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* com.jonah.service..*(..))")
    public void servicePointcut() {

    }

    /* Single @Around advice handles everything:* - Logs method entry with arguments
     * - Logs method exit with return value
     * - Logs exceptions with stack trace
     * - Tracks execution time
    */
    @Around("servicePointcut()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws  Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        long start = System.currentTimeMillis();

        log.info("Entering {}.{} | Args: {}", className, methodName,
                Arrays.stream(args)
                        .map(arg -> arg != null ? arg.toString() : "null")
                        .toArray());

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;

            log.info("Exiting: {}.{}() | Duration: {} ms", className, methodName, duration);

            log.debug("Results: {}", result);

            return  result;
        } catch (Exception e){
            long duration = System.currentTimeMillis() - start;

            log.error("Exception in: {}.{}() | Duration {} ms: | Error: {}", className, methodName, duration, e.getMessage(), e);


            throw  e; // Do not swallow the exception
        }
    }
}

