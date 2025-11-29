package com.jonah.utils;

import jakarta.persistence.Id;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class LoggingUtil {
    /**
     * Safely log objects without exposing sensitive data
     */
    public static String safeToLog(Object obj) {
        if (obj == null) return "null";

        Class<?> clazz = obj.getClass();

        // Simple types - log as is
        if (isSimpleType(clazz, obj)) {
            return obj.toString();
        }

        // Arrays - log length only
        if (clazz.isArray()) {
            return "Array[length=" + Array.getLength(obj) + "]";
        }

        // Collections - log size only
        if (obj instanceof Collection<?> col) {
            return col.getClass().getSimpleName() + "[size=" + col.size() + "]";
        }

        // Maps - log size only
        if (obj instanceof Map<?, ?> map) {
            return map.getClass().getSimpleName() + "[size=" + map.size() + "]";
        }

        // JPA entities - extract @Id only (avoids PII)
        Field idField = getIdField(clazz);
        if (idField != null) {
            try {
                idField.setAccessible(true);
                Object idValue = idField.get(obj);
                return clazz.getSimpleName() + "(id=" + idValue + ")";
            } catch (IllegalAccessException ignored) {}
        }

        // Fallback - class name only
        return clazz.getSimpleName();
    }

    private static boolean isSimpleType(Class<?> clazz, Object obj) {
        return clazz.isPrimitive() ||
                obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj instanceof Enum ||
                obj instanceof java.util.Date ||
                obj instanceof java.time.temporal.TemporalAccessor;
    }

    private static Field getIdField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }
}
