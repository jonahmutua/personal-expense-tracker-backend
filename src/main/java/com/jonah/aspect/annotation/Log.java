package com.jonah.aspect.annotation;

import java.lang.annotation.*;

/**
 * Apply @Log at CLASS or METHOD level
 *
 * Class level: All public methods in the class are logged
 * Method level: Only that specific method is logged
 *
 * Method level overrides class level
 */
@Target({ElementType.TYPE, ElementType.METHOD})  // ← Both CLASS and METHOD
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * If false, this specific method is excluded from logging
     * (useful when @Log is on class but you want to skip one method)
     */
    boolean enabled() default true;
}
