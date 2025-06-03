package org.qlspringframework.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author jixu
 * @title Autowired
 * @date 2025/6/1 16:50
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

}
