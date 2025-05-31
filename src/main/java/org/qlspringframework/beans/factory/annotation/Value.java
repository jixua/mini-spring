package org.qlspringframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author jixu
 * @title Value
 * @date 2025/5/31 16:13
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value();
}
