package org.qlspringframework.stereotype;

import java.lang.annotation.*;

/**
 * @author jixu
 * @title Component
 * @date 2025/5/31 13:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
