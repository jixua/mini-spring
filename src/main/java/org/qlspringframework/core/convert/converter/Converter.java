package org.qlspringframework.core.convert.converter;

/**
 * @author jixu
 * @title Converter
 * @date 2025/6/3 15:45
 */
public interface Converter<S,T> {
    T convert(S source);
}
