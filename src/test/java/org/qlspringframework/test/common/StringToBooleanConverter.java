package org.qlspringframework.test.common;

import org.qlspringframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author jixu
 * @title StringToBooleanConverter
 * @date 2025/6/3 20:20
 */
public class StringToBooleanConverter implements GenericConverter {
    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        return Boolean.valueOf((String) source);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Boolean.class));
    }
}
