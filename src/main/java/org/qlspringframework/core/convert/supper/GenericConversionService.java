package org.qlspringframework.core.convert.supper;

import org.qlspringframework.core.convert.ConversionService;
import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.converter.ConverterFactory;
import org.qlspringframework.core.convert.converter.ConverterRegister;
import org.qlspringframework.core.convert.converter.GenericConverter;
import org.qlspringframework.core.convert.converter.GenericConverter.ConvertiblePair;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.aspectj.apache.bcel.Constants.types;

/**
 * 通用转换服务类，实现ConversionService接口和ConverterRegister接口
 * 提供类型转换功能和转换器注册功能
 *
 * @author jixu
 * @title GenericConversionService
 * @date 2025/6/3 17:14
 */
public class GenericConversionService implements ConversionService, ConverterRegister {

    // 用于存储通用转换器
    private Map<ConvertiblePair,GenericConverter> converters = new HashMap();


    /**
     * 检查是否可以将源类型转换为目标类型
     *
     * @param sourceType 源类型的Class对象
     * @param targetType 目标类型的Class对象
     * @return 如果可以进行转换则返回true，否则返回false
     */
    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    /**
     * 将给定的源对象转换为目标类型
     *
     * @param source     要转换的源对象
     * @param targetType 目标类型的Class对象
     * @return 转换后的目标类型对象
     */
    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        GenericConverter converter = getConverter(sourceType, targetType);
        return (T) converter.convert(source, sourceType, targetType);
    }

    /**
     * 获取通用适配器
     * @param sourceType 源类型的Class对象
     * @param targetType 目标类型的Class对象
     * @return 对应的GenericConverter对象，如果不存在则返回null
     */
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType){
        List<Class<?>> sourceHierarchy = getClassHierarchy(sourceType);
        List<Class<?>> targetHierarchy = getClassHierarchy(targetType);

        for (Class<?> sourceCandidate : sourceHierarchy) {
            for (Class<?> targetCandidate : targetHierarchy) {
                ConvertiblePair convertiblePair = new ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter genericConverter = converters.get(convertiblePair);
                if (genericConverter != null){
                    return genericConverter;
                }
            }

        }
        return null;
    }

    // 获取目标类的完整继承层级结构，以便于通过ConvertiblePair判断转换器是否已注册
    protected List<Class<?>> getClassHierarchy(Class<?> clazz){
        List<Class<?>>  hierarchy = new ArrayList<>();
        while ((clazz != null)){
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }


    @Override
    public void addConverter(Converter<?, ?> converter) {
        // 这里传入的是一个Converter转换器对象，我们需要将其转化为GenericConverter对象
        ConvertiblePair requiredTypeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(converter, requiredTypeInfo);
        for (ConvertiblePair convertibleType : converterAdapter.getConvertibleTypes()) {
            converters.put(convertibleType,converterAdapter);
        }

    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (ConvertiblePair convertibleType : converter.getConvertibleTypes()) {
            converters.put(convertibleType, converter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory converterFactory) {
        ConvertiblePair requiredTypeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(converterFactory, requiredTypeInfo);
        for (ConvertiblePair convertibleType : converterFactoryAdapter.getConvertibleTypes()) {
            converters.put(convertibleType,converterFactoryAdapter);
        }
    }


    /**
     * 获取到目标类所实现的泛型接口的元素
     * @param object 要分析的对象
     * @return ConvertiblePair对象，包含源类型和目标类型
     */
    private ConvertiblePair getRequiredTypeInfo(Object object){
        Type[] types = object.getClass().getGenericInterfaces();
        ParameterizedType parameterized = (ParameterizedType) types[0];
        Type[] actualTypeArguments = parameterized.getActualTypeArguments();
        Class sourceType = (Class) actualTypeArguments[0];
        Class targetType = (Class) actualTypeArguments[1];
        return new ConvertiblePair(sourceType, targetType);
    }


    // 通过适配器模式对Converter以及ConverterFactory进行适配
    private final class ConverterAdapter implements GenericConverter{

        private final Converter converter;

        private final ConvertiblePair convertiblePair;

        private ConverterAdapter(Converter converter, ConvertiblePair convertiblePair) {
            this.converter = converter;
            this.convertiblePair = convertiblePair;
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(convertiblePair);
        }
    }

    private final class ConverterFactoryAdapter implements GenericConverter{
        private final ConverterFactory converterFactory;

        private final ConvertiblePair convertiblePair;

        private ConverterFactoryAdapter(ConverterFactory converterFactory, ConvertiblePair convertiblePair) {
            this.converterFactory = converterFactory;
            this.convertiblePair = convertiblePair;
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(convertiblePair);
        }
    }
}
