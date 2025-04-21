package org.qlspringframework.beans.factory;

/**
 * HierarchicalBeanFactory 接口扩展了 BeanFactory 接口，提供了对层次结构中的 Bean 工厂的支持。
 * 该接口允许访问父 Bean 工厂，并支持在层次结构中查找 Bean 定义。
 *
 * 通过实现该接口，Bean 工厂可以形成一个层次结构，子工厂可以继承父工厂的配置和 Bean 定义。
 * 这在需要分层次管理 Bean 定义的场景中非常有用，例如在 Spring 框架中。
 *
 * 该接口本身不定义任何方法，而是作为标记接口，表示该 Bean 工厂支持层次结构。
 */
public interface HierarchicalBeanFactory extends BeanFactory{
}

