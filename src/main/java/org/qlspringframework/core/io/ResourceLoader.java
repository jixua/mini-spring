package org.qlspringframework.core.io;

/**
 * ResourceLoader接口用于定义资源加载的通用规范
 * 它提供了一种统一的资源访问方式，使得应用程序可以灵活地从不同的位置加载资源
 *
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
