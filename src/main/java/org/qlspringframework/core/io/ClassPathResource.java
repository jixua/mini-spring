package org.qlspringframework.core.io;

import java.io.InputStream;

/**
 * @description: 用于加载类路径当中的资源文件 如resources文件夹下的资源
 * @author: jixu
 * @create: 2025-04-08 18:14
 **/
public class ClassPathResource implements Resource{

    // 文件相对路径
    private final String path;

    public ClassPathResource(String path) {
        this.path = path;
    }


    @Override
    public InputStream getInputStream() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null){

        }
        return inputStream;

    }
}
