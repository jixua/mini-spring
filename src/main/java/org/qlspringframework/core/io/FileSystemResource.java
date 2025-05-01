package org.qlspringframework.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * FileSystemResource 类实现 Resource 接口，用于加载文件系统中的资源。
 * 它通过文件路径来访问资源，并提供获取资源输入流的功能。
 *
 * @author jixu
 * @create 2025-04-09 15:04
 */
public class FileSystemResource implements Resource{

    // 文件路径字符串
    private final String filePath;

    /**
     * 构造函数，初始化 FileSystemResource 对象。
     *
     * @param filePath 文件路径
     */
    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取资源的输入流。
     * 该方法通过文件路径创建一个 Path 对象，并使用该 Path 对象获取输入流。
     * 如果文件不存在，会抛出 FileNotFoundException 异常。
     *
     * @return 资源的输入流
     * @throws FileNotFoundException 如果文件不存在
     */
    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        try {
            Path path = new File(this.filePath).toPath();
            InputStream inputStream = Files.newInputStream(path);
            return inputStream;
        } catch (IOException e) {
            // 当文件不存在时，抛出自定义的 FileNotFoundException 异常
            throw new FileNotFoundException(String.format("%s，文件不存在",this.filePath));
        }
    }
}
