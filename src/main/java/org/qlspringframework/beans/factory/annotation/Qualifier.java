package org.qlspringframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author jixu
 * @title Qualifier
 * @date 2025/6/1 16:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
public @interface Qualifier {
    String value() default "";
}
