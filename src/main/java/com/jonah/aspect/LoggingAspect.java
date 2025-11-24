package com.jonah.aspect;

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
                        .map(this::safeToLog)
                        .toArray());

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;

            log.info("Exiting: {}.{}() | Duration: {} ms", className, methodName, duration);

            log.debug("Results: {}", safeToLog(result));

            return  result;
        } catch (Exception e){
            long duration = System.currentTimeMillis() - start;

            log.error("Exception in: {}.{}() | Duration {} ms: | Error: {}", className, methodName, duration, e.getMessage(), e);


            throw  e; // Do not swallow the exception
        }
    }

    private String safeToLog(Object obj) {
        if (obj == null) return "null";

        Class<?> clazz = obj.getClass();

        // Simple types
        if (clazz.isPrimitive() ||
                obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj instanceof Enum ||
                obj instanceof java.util.Date ||
                obj instanceof java.time.temporal.TemporalAccessor) {
            return obj.toString();
        }

        // Arrays
        if (clazz.isArray()) {
            return "Array[length=" + Array.getLength(obj) + "]";
        }

        // Collections / Maps
        if (obj instanceof Collection<?> col) {
            return "Collection[size=" + col.size() + "]";
        }

        if (obj instanceof Map<?, ?> map) {
            return "Map[size=" + map.size() + "]";
        }

        // JPA entity extract @Id only
        try {
            Field idField = getIdField(clazz);
            if (idField != null) {
                idField.setAccessible(true);
                Object idValue = idField.get(obj);
                return clazz.getSimpleName() + "(id=" + idValue + ")";
            }
        } catch (Exception ignored) {}

        // Fallback: log the class name only (avoids recursion / PII)
        return clazz.getSimpleName();
    }

    private Field getIdField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }
}

