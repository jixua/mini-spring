package org.qlspringframework.beans.factory;

/**
 * BeanFactoryAware接口是Spring Framework中的一部分，用于让实现该接口的bean在创建时能够获得BeanFactory的引用。
 * 这个接口主要用于那些需要访问BeanFactory的组件，以便获取其他bean或者执行某些与bean管理相关的操作。
 * 实现该接口的类需要提供setBeanFactory方法的具体实现，以便在bean初始化时接收BeanFactory的引用。
 *
 * @author jixu
 * @title BeanFactoryAware
 * @date 2025/5/5 17:06
 */
public interface BeanFactoryAware extends Aware{

    public void setBeanFactory(BeanFactory beanFactory);
}
