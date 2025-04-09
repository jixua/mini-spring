package org.qlspringframework.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * @description: 路径文件加载
 * @author: jixu
 * @create: 2025-04-09 15:04
 **/
public class FileSystemResource implements Resource{

    private final String filePath;

    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取资源的输入流。
     *
     * @return 资源的输入流
     */
    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        try {
            Path path = new File(this.filePath).toPath();
            InputStream inputStream = Files.newInputStream(path);
            return inputStream;
        } catch (IOException e) {
            throw new FileNotFoundException(String.format("%s，文件不存在",this.filePath));
        }
    }
}
