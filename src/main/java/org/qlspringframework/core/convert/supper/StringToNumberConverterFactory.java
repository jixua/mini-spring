package org.qlspringframework.core.convert.supper;

import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.converter.ConverterFactory;

/**
 * 字符串到数字类型的转换工厂类
 *
 * 该类实现了ConverterFactory接口，用于创建将字符串转换为各种数字类型的转换器
 * 主要功能是根据目标类型获取相应的转换器对象
 *
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


    /**
     * 字符串到数字类型转换器的静态内部类
     *
     * 该类实现了Converter接口，用于将字符串转换为指定的数字类型
     * 主要功能包括：根据目标类型转换字符串，并处理空字符串和不支持的类型
     *
     * @param <T> 目标数字类型的泛型参数，限定为Number的子类
     */
    public static final class StringToNumber<T extends Number> implements  Converter<String,T>{

        private final Class<T> targetType;

        /**
         * 构造方法，初始化目标类型
         *
         * @param targetType 目标类型的Class对象，用于指定转换后的数据类型
         */
        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        /**
         * 转换字符串到目标数字类型
         *
         * 该方法首先检查字符串是否为空，如果为空则返回null
         * 根据目标类型，将字符串转换为相应的数字类型
         * 如果目标类型不支持，则抛出IllegalArgumentException异常
         *
         * @param source 源字符串，需要被转换为数字类型
         * @return 转换后的目标数字类型对象，如果字符串为空或类型不匹配则返回null或抛出异常
         */
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
