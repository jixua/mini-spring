package org.qlspringframework.beans.factory;

/**
 * FactoryBean接口定义了工厂方法模式的实现，用于在Spring框架中创建对象。
 * 实现该接口的类可以自定义对象的创建过程，而不仅仅是通过构造器来创建。
 *
 * @author jixu
 * @title FactoryBean
 * @date 2025/5/20 14:00
 */
public interface FactoryBean<T> {

    /**
     * 获取由FactoryBean创建的对象。
     *
     * @return 创建的对象实例。
     */
    T getObject();

    /**
     * 判断由FactoryBean创建的对象是单例还是多例。
     *
     * @return 如果对象是单例，则返回true；否则返回false。
     */
    boolean isSingleton();
}

