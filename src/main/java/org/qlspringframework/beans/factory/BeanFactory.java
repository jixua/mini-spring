package org.qlspringframework.beans.factory;

/**
 * BeanFactory 接口定义了获取 Bean 实例的方法。
 * 该接口是 Spring 框架中用于管理 Bean 的核心接口之一。
 */
public interface BeanFactory {

    /**
     * 根据指定的 Bean 名称获取对应的 Bean 实例。
     *
     * @param name Bean 的名称，通常是在 Spring 配置文件中定义的 Bean 的 ID 或名称。
     * @return 返回与指定名称对应的 Bean 实例。如果找不到对应的 Bean，可能会抛出异常。
     */
    public Object getBean(String name);


    public <T> T getBean(String name, Class<T> requiredType);

}
