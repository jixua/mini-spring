package org.qlspringframework.core.convert.converter;

/**
 * Converter接口定义了类型转换的标准规范
 * 它提供了一个将源类型S转换为目标类型T的方法
 * 主要用于在两种不同类型之间进行数据转换或映射
 *
 * @author jixu
 * @title Converter
 * @date 2025/6/3 15:45
 */
public interface Converter<S,T> {
    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象，类型为S，是需要被转换的原始数据
     * @return 转换后的目标对象，类型为T
     */
    T convert(S source);
}

