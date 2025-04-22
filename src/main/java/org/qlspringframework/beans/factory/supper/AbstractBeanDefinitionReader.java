package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.core.io.DefaultResourceLoader;
import org.qlspringframework.core.io.ResourceLoader;

/**
 * AbstractBeanDefinitionReader 是一个抽象类，实现了 BeanDefinitionReader 接口，
 * 用于读取和注册 BeanDefinition。它提供了资源加载和 BeanDefinition 注册的基本功能。
 *
 * @author: jixu
 * @create: 2025-04-10 14:06
 **/
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private ResourceLoader resourceLoader;

    private final BeanDefinitionRegister beanDefinitionRegister;

    /**
     * 构造函数，初始化 AbstractBeanDefinitionReader 实例。
     *
     * @param beanDefinitionRegister 用于注册 BeanDefinition 的实例
     */
    protected AbstractBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        this.resourceLoader = new DefaultResourceLoader();
        this.beanDefinitionRegister = beanDefinitionRegister;
    }

    /**
     * 根据指定的多个资源位置加载 BeanDefinition。
     *
     * @param locations 资源的位置数组，通常为文件路径或URL数组
     */
    @Override
    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    /**
     * 获取 BeanDefinitionRegister 实例。
     *
     * @return BeanDefinitionRegister 用于注册 BeanDefinition 的实例
     */
    @Override
    public BeanDefinitionRegister getRegistry() {
        return beanDefinitionRegister;
    }

    /**
     * 获取 ResourceLoader 实例。
     *
     * @return ResourceLoader 用于加载资源的实例
     */
    @Override
    public ResourceLoader getResourceLoad() {
        return resourceLoader;
    }

    /**
     * 设置 ResourceLoader 实例。
     *
     * @param resourceLoader 用于加载资源的实例
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
