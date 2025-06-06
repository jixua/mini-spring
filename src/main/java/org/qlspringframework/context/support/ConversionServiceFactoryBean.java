package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.FactoryBean;
import org.qlspringframework.beans.factory.InitializingBean;
import org.qlspringframework.core.convert.ConversionService;
import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.converter.ConverterFactory;
import org.qlspringframework.core.convert.converter.ConverterRegister;
import org.qlspringframework.core.convert.converter.GenericConverter;
import org.qlspringframework.core.convert.supper.DefaultConversionService;
import org.qlspringframework.core.convert.supper.GenericConversionService;

import java.util.Set;

/**
 * @author jixu
 * @title ConversionServiceFactoryBean
 * @date 2025/6/5 19:12
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService> , InitializingBean {

    private Set<?> converters;

    private GenericConversionService conversionService;


    /**
     * 获取由FactoryBean创建的对象。
     *
     * @return 创建的对象实例。
     */
    @Override
    public ConversionService getObject() {
        return null;
    }

    /**
     * 判断由FactoryBean创建的对象是单例还是多例。
     *
     * @return 如果对象是单例，则返回true；否则返回false。
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        conversionService = new DefaultConversionService();
        registerConverters(converters,conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegister registry) {
        for (Object converter : converters) {
            if (converter instanceof GenericConverter) {
                registry.addConverter((GenericConverter) converter);
            } else if (converter instanceof Converter<?, ?>) {
                registry.addConverter((Converter<?, ?>) converter);
            } else if (converter instanceof ConverterFactory<?, ?>) {
                registry.addConverterFactory((ConverterFactory<?, ?>) converter);
            } else {
                throw new IllegalArgumentException("Each converter object must implement one of the " +
                        "Converter, ConverterFactory, or GenericConverter interfaces");
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
