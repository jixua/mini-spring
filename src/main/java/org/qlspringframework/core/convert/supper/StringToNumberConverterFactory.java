package org.qlspringframework.core.convert.supper;

import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.converter.ConverterFactory;

/**
 * @author jixu
 * @title StringToNumberConverterFactory
 * @date 2025/6/3 16:11
 */
public class StringToNumberConverterFactory implements ConverterFactory<String,Number> {
    /**
     * 根据目标类型获取转换器对象
     *
     * @param targetType 目标类型的Class对象，用于指定转换后的数据类型
     * @return 返回一个Converter对象，用于将源数据类型S转换为目标类型T
     */
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<T>(targetType);
    }


    public static final class StringToNumber<T extends Number> implements  Converter<String,T>{

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source.length() == 0){
                return  null;
            }

            if (targetType.equals(Integer.class)){
                return (T) Integer.valueOf(source);
            } else if (targetType.equals(Long.class)) {
                return (T) Long.valueOf(source);
            }else {
                throw new IllegalArgumentException(
                        "Cannot convert String [" + source + "] to target class [" + targetType.getName() + "]");
            }
        }
    }
}
