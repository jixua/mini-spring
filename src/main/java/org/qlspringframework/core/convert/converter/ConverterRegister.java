package org.qlspringframework.core.convert.converter;

/**
 * ConverterRegister接口定义了转换器注册的契约，
 * 允许在运行时动态添加新的转换器或转换器工厂。
 * 这个接口的主要目的是提供一个统一的入口点，
 * 以便在应用程序中注册和使用各种类型的转换器。
 *
 * @author jixu
 * @title ConverterRegister
 * @date 2025/6/3 19:17
 */
public interface ConverterRegister {

    /**
     * 添加一个转换器到注册表中。
     * 这个方法允许在运行时动态添加新的转换器，
     * 以便在应用程序中使用。
     *
     * @param converter 要添加的转换器，类型为Converter的泛型实例。
     */
    void addConverter(Converter<?, ?> converter);

    /**
     * 添加一个通用转换器到注册表中。
     * 通用转换器能够处理多个源和目标类型，
     * 这个方法允许在运行时动态添加这样的转换器。
     *
     * @param genericConverter 要添加的通用转换器，类型为GenericConverter。
     */
    void addConverter(GenericConverter genericConverter);

    /**
     * 添加一个转换器工厂到注册表中。
     * 转换器工厂可以根据需要生成多个转换器，
     * 这个方法允许在运行时动态添加这样的工厂。
     *
     * @param converterFactory 要添加的转换器工厂，类型为ConverterFactory。
     */
    void addConverterFactory(ConverterFactory converterFactory);
}

