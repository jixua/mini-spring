package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.core.io.DefaultResourceLoader;
import org.qlspringframework.core.io.ResourceLoader;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-10 14:06
 **/
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private ResourceLoader resourceLoader;

    private final BeanDefinitionRegister beanDefinitionRegister;

    /**
     * beanDefinitionRegister是用来注册BeanDefinition使用的
     * 其子类DefaultListableBeanFactory实现了beanDefinitionRegister与BeanFactory
     * 可以通过DefaultListableBeanFactory获取、创建Bean
     * @param beanDefinitionRegister 用于注册BeanDefinition的实例
     */
    protected AbstractBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        this.resourceLoader = new DefaultResourceLoader();
        this.beanDefinitionRegister = beanDefinitionRegister;
    }

    /**
     * 根据指定的多个资源位置加载BeanDefinition。
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
     * 获取BeanDefinitionRegister实例。
     *
     * @return BeanDefinitionRegister 用于注册BeanDefinition的实例
     */
    @Override
    public BeanDefinitionRegister getRegistry() {
        return beanDefinitionRegister;
    }

    /**
     * 获取ResourceLoader实例。
     *
     * @return ResourceLoader 用于加载资源的实例
     */
    @Override
    public ResourceLoader getResourceLoad() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
