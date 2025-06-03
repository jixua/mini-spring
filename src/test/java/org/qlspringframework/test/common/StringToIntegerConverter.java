package org.qlspringframework.test.common;

import org.qlspringframework.core.convert.converter.Converter;

/**
 * @author jixu
 * @title StringToIntegerConverter
 * @date 2025/6/3 16:29
 */
public class StringToIntegerConverter implements Converter<String,Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
