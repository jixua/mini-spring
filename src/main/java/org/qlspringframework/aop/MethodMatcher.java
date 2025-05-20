package org.qlspringframework.aop;

import java.lang.reflect.Method;

/**
 * MethodMatcher接口用于定义方法匹配规则。
 * 它主要用于判断一个给定的方法是否与当前上下文中的方法匹配。
 * 这个接口在需要对方法进行筛选或条件判断时非常有用，
 * 例如在AOP（面向切面编程）中，确定哪些方法需要应用切面逻辑。
 *
 * @author jixu
 * @title MethodMatcher
 * @date 2025/5/20 15:46
 */
public interface MethodMatcher {

    /**
     * 判断给定的方法是否与当前上下文中的方法匹配。
     *
     * @param method 要检查的方法
     * @param targetClass 目标类，即方法所属的类
     * @return 如果方法匹配则返回true，否则返回false
     */
    boolean matches(Method method , Class<?> targetClass);
}
