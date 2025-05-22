package org.qlspringframework.aop.framework;

/**
 * AopProxy接口定义了获取代理对象的方法
 * 代理对象可以是JDK动态代理、CGLIB代理或其他类型的代理对象
 * 主要用于在AOP（面向切面编程）中创建和管理代理对象
 *
 * @author jixu
 * @title AopProxy
 * @date 2025/5/21 21:51
 */
public interface AopProxy {

    /**
     * 获取代理对象
     *
     * @return 代理对象，通过该对象可以调用目标方法以及切面方法
     */
    Object getProxy();
}
