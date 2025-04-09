package org.qlspringframework.core.io;
/**
 * @description: 资源加载器，用于加载Resource的实现类
 * @author: jixu
 * @create: 2025-04-09 10:49
 **/
public interface ResourceLoader {
    /**
     * 根据指定的位置获取资源
     *
     * @param location 资源的位置，通常是一个路径或URL
     * @return 返回一个Resource对象，表示加载的资源
     */
    Resource getResource(String location);
}
