package org.qlspringframework.core.io;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-09 10:47
 **/
public class DefaultResourceLoader implements ResourceLoader{

    private final String CLASSPATH_URL_PREFIX = "classpath:";


    /**
     * 根据指定的位置获取资源
     *
     * @param location 资源的位置，通常是一个路径或URL
     * @return 返回一个Resource对象，表示加载的资源
     */
    @Override
    public Resource getResource(String location) {

        // 加载ClassPath路径下的资源文件
        if (location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }else {
            // TODO:
        }

        return null;
    }
}
