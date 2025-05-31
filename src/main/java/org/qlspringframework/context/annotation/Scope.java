package org.qlspringframework.context.annotation;

import java.lang.annotation.*;

/**
 * @author jixu
 * @title Scope
 * @date 2025/5/31 13:45
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value() default "singleton";
}
