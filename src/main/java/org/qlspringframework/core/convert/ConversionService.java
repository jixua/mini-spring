package org.qlspringframework.core.convert;

/**
 * ConversionService接口用于定义类型转换的服务
 * 它提供了机制来检查是否可以执行从一种类型到另一种类型的转换，
 * 并执行实际的转换操作
 *
 * @author jixu
 * @title ConversionService
 * @date 2025/6/3 17:02
 */
public interface ConversionService {

    /**
     * 检查是否可以将源类型转换为目标类型
     *
     * @param sourceType 源类型的Class对象
     * @param targetType 目标类型的Class对象
     * @return 如果可以进行转换则返回true，否则返回false
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    /**
     * 将给定的源对象转换为目标类型
     *
     * @param source 要转换的源对象
     * @param targetType 目标类型的Class对象
     * @param <T> 目标类型的泛型参数
     * @return 转换后的目标类型对象
     */
    <T> T convert(Object source, Class<T> targetType);
}
