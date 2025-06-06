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


    /**
     * 根据bean的名称和类型获取指定的bean实例
     *
     * @param name bean的名称，用于标识特定的bean实例
     * @param requiredType 指定的bean类型，用于确保返回的bean实例是所需的类型
     * @param <T> 泛型参数，表示所需的bean类型
     * @return 返回指定类型的bean实例
     */
    public <T> T getBean(String name, Class<T> requiredType);

    /**
     * 根据指定的类型获取bean实例当有多个相同类型的bean时，此方法可能抛出异常
     *
     * @param requiredType 指定的bean类型，用于确保返回的bean实例是所需的类型
     * @param <T> 泛型参数，表示所需的bean类型
     * @return 返回指定类型的bean实例
     */
    public <T> T getBean(Class<T> requiredType);


    boolean containsBean(String name);
}
