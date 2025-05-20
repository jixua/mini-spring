package org.qlspringframework.beans.factory;

/**
 * @author jixu
 * @title FactoryBean
 * @date 2025/5/20 14:00
 */
public interface FactoryBean<T> {

    T getObject();

    boolean isSingleton();
}
