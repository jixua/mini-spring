package org.qlspringframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * URLResource类实现了Resource接口，用于表示和访问通过URL定位的资源。
 * 它提供了一种获取资源输入流的方法，以便进一步处理资源内容。
 */
public class URLResource implements Resource{
    // URL资源的URL对象
    private final URL url;

    /**
     * 构造一个URLResource实例。
     *
     * @param url 资源的URL
     */
    public URLResource(URL url) {
        this.url = url;
    }

    /**
     * 获取资源的输入流。
     * 该方法通过URL连接获取输入流，用于读取资源内容。
     *
     * @return 资源的输入流
     * @throws IOException 如果无法创建输入流
     */
    @Override
    public InputStream getInputStream() throws IOException {
        // 打开URL连接以便获取输入流
        URLConnection urlConnection = this.url.openConnection();
        // 初始化输入流变量
        InputStream inputStream = null;
        // 从URL连接获取输入流
        inputStream = urlConnection.getInputStream();
        // 返回输入流
        return inputStream;
    }
}
