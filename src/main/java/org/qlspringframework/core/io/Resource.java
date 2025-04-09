package org.qlspringframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 资源接口，定义了获取输入流的方法。
 * @description: 定义资源的基本操作
 * @author: jixu
 * @create: 2025-04-08 18:12
 **/
public interface Resource {
    /**
     * 获取资源的输入流。
     * @return 资源的输入流
     */
    InputStream getInputStream() throws IOException;

}