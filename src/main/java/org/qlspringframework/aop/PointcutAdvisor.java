package org.qlspringframework.aop;

/**
 * PointcutAdvisor接口是用于获取切点（PointCut）的顾问（Advisor）接口。
 * 它继承自Advisor接口，主要作用是定义一个获取切点的方法。
 * 切点（PointCut）是用来定义哪些类或方法需要应用通知（Advice）的模式。
 *
 * @author jixu
 * @title PointcutAdvisor
 * @date 2025/5/27 21:03
 */
public interface PointcutAdvisor extends Advisor{

    /**
     * 获取切点（PointCut）对象。
     *
     * @return PointCut对象，表示一个切点。
     */
    PointCut getPointcut();
}
