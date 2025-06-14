package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.core.io.Resource;
import org.qlspringframework.core.io.ResourceLoader;

/**
 * BeanDefinitionReader接口用于读取和加载Bean定义。
 * 它提供了获取BeanDefinitionRegister和ResourceLoader实例的方法，
 * 以及根据资源位置或Resource对象加载Bean定义的方法。
 */
public interface BeanDefinitionReader {

    /**
     * 获取BeanDefinitionRegister实例。
     * @return BeanDefinitionRegister 用于注册BeanDefinition的实例
     */
    BeanDefinitionRegister getRegistry();

    /**
     * 获取ResourceLoader实例。
     * @return ResourceLoader 用于加载资源的实例
     */
    ResourceLoader getResourceLoad();

    /**
     * 根据指定的资源位置加载BeanDefinition。
     * @param location 资源的位置，通常为文件路径或URL
     */
    void loadBeanDefinitions(String location);

    /**
     * 根据指定的多个资源位置加载BeanDefinition。
     * @param locations 资源的位置数组，通常为文件路径或URL数组
     */
    void loadBeanDefinitions(String[] locations);

    /**
     * 根据指定的Resource对象加载BeanDefinition。
     * @param resource 资源对象，包含具体的资源信息
     */
    void loadBeanDefinitions(Resource resource);



}
