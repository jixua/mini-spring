package org.qlspringframework.beans.ioc.ioc;

import cn.hutool.core.io.IoUtil;
import org.junit.Test;
import org.qlspringframework.core.io.DefaultResourceLoader;
import org.qlspringframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-09 11:04
 **/
public class ResourceLoadTest {

    @Test
    public void testClassPathResource() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String read = IoUtil.readUtf8(inputStream);
        System.out.println(read);
    }

    @Test
    public void testFileSystemResource() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("src/test/resources/hello.txt");
        InputStream inputStream = resource.getInputStream();
        String read = IoUtil.readUtf8(inputStream);
        System.out.println(read);

    }

    @Test
    public void testURLResource() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("https://gitee.com/y_project/RuoYi/blob/master/README.md");
        InputStream inputStream = resource.getInputStream();
        String read = IoUtil.readUtf8(inputStream);
        System.out.println(read);

    }
}
