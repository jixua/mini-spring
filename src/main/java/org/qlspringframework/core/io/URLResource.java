package org.qlspringframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-09 15:11
 **/
public class URLResource implements Resource{
    private final URL url;

    public URLResource(URL url) {
        this.url = url;
    }

    /**
     * 获取资源的输入流。
     *
     * @return 资源的输入流
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection urlConnection = this.url.openConnection();
        InputStream inputStream = null;
        inputStream = urlConnection.getInputStream();
        return inputStream;

    }
}
