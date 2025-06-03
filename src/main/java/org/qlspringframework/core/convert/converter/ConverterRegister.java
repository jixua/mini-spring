package org.qlspringframework.core.convert.converter;

/**
 * @author jixu
 * @title ConverterRegister
 * @date 2025/6/3 19:17
 */
public interface ConverterRegister {
    void addConverter(Converter<?, ?> converter);

    void addConverterFactory(ConverterFactory converterFactory);
}
