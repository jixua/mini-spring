package org.qlspringframework.aop;

/**
 * 定义切点接口，用于选择性地应用通知（Advice）到目标类的特定方法上
 * 切点通过类过滤器（ClassFilter）和方法匹配器（MethodMatcher）来确定应用通知的方法
 *
 * @author jixu
 * @title PointCut
 * @date 2025/5/20 15:47
 */
public interface PointCut {

    /**
     * 获取类过滤器，用于判断哪些类需要应用通知
     *
     * @return ClassFilter 实例，用于过滤目标类
     */
    ClassFilter getClassFilter();

    /**
     * 获取方法匹配器，用于判断目标类中的哪些方法需要应用通知
     *
     * @return MethodMatcher 实例，用于匹配目标方法
     */
    MethodMatcher getMethodMatcher();
}
