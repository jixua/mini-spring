package org.qlspringframework.beans.ioc;

import cn.hutool.core.io.IoUtil;
import org.junit.Test;
import org.qlspringframework.core.io.ClassPathResource;
import org.qlspringframework.core.io.DefaultResourceLoader;
import org.qlspringframework.core.io.Resource;

import java.io.InputStream;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-09 11:04
 **/
public class ResourceLoadTest {

    @Test
    public void testClassPathResource(){
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String read = IoUtil.readUtf8(inputStream);
        System.out.println(read);
    }
}
