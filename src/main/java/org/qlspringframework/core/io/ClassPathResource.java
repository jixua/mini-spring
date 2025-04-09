package org.qlspringframework.core.io;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @description: 用于加载类路径当中的资源文件 如resources文件夹下的资源
 * @author: jixu
 * @create: 2025-04-08 18:14
 **/
public class ClassPathResource implements Resource{

    // 文件相对路径
    private final String path;
    private final ClassLoader classLoader;

    public ClassPathResource(String path) {
        this.path = path;
        this.classLoader = this.getClass().getClassLoader();
    }


    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (inputStream == null){
            throw new FileNotFoundException(String.format("%s，文件不存在",this.path));
        }
        return inputStream;

    }
}
