package org.qlspringframework.context;

/**
 * ConfigurableApplicationContext 接口扩展了 ApplicationContext 接口，提供了对应用上下文配置的支持。
 * 该接口允许在应用上下文初始化之前或之后进行配置，例如设置父上下文、刷新上下文等操作。
 *
 * 实现该接口的类通常用于管理 Spring 应用上下文的生命周期和配置。
 *
 * @see ApplicationContext
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
    /**
     * 刷新容器，重新加载并初始化所有配置和Bean
     */
    void refresh();
}
