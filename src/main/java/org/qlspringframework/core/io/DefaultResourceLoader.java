package org.qlspringframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-09 10:47
 **/
public class DefaultResourceLoader implements ResourceLoader{

    // Classpath前缀
    private final String CLASSPATH_URL_PREFIX = "classpath:";


    /**
     * 根据指定的位置获取资源
     * 目前只实现了Classpath,URL,File
     *
     * @param location 资源的位置，通常是一个路径或URL
     * @return 返回一个Resource对象，表示加载的资源
     */
    @Override
    public Resource getResource(String location) {

        // 加载ClassPath路径下的资源文件
        if (location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        // 加载路径资源文件
        else if (location.startsWith("/")){
            return new FileSystemResource(location.substring(1));
        }
        // 加载URl资源文件
        else {
            try {
                URL url = new URL(location);
                return new URLResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }

    }
}
