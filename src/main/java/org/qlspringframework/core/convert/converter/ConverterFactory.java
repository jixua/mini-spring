package org.qlspringframework.core.convert.converter;

/**
 * ConverterFactory接口用于创建转换器对象，以支持不同类型的数据转换
 * 它提供了一种通用机制，可以根据目标类型获取相应的转换器
 *
 * @param <S> 输入类型参数，表示需要转换的源数据类型
 * @param <R> 输出类型参数的父类型，表示转换后的目标数据类型的父类型
 *
 * @author jixu
 * @title ConverterFactory
 * @date 2025/6/3 16:08
 */
public interface ConverterFactory<S, R> {

    /**
     * 根据目标类型获取转换器对象
     *
     * @param <T> 目标类型参数，表示具体的转换后数据类型，必须是R类型或其子类型
     * @param targetType 目标类型的Class对象，用于指定转换后的数据类型
     * @return 返回一个Converter对象，用于将源数据类型S转换为目标类型T
     */
    <T extends R> Converter<S,T> getConverter(Class<T> targetType);
}

