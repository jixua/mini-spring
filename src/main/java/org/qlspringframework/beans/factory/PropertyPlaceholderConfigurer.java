package org.qlspringframework.beans.factory;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanFactoryPostProcessor;
import org.qlspringframework.core.io.DefaultResourceLoader;
import org.qlspringframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;


/**
 * PropertyPlaceholderConfigurer 类实现 BeanFactoryPostProcessor 接口，
 * 用于解析并替换 Bean 定义中的占位符。
 *
 * 该类主要功能是加载属性文件，并在 BeanFactory 中的所有 Bean 定义属性中替换相应的占位符。
 *
 * @author jixu
 * @title PropertyPlaceholderConfigurer
 * @date 2025/5/31 00:35
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    // 占位符前缀
    public static final String PLACEHOLDER_PREFIX = "${";

    // 占位符后缀
    public static final String PLACEHOLDER_SUFFIX = "}";

    // 属性文件路径
    private String location;

    /**
     * 对 BeanFactory 进行后处理的方法。该方法在 Spring 容器实例化所有 bean 之后，但在 bean 初始化之前被调用。
     * 实现类可以通过该方法对 BeanFactory 进行自定义的修改或扩展。
     *
     * @param beanFactory 可配置的 BeanFactory 实例，允许对 bean 定义进行修改或扩展。
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // 加载属性配置文件
        Properties properties = loadProperties();

        // 属性值替换占位符
        processProperties(beanFactory, properties);
    }

    /**
     * 处理属性，替换 BeanFactory 中所有 Bean 定义中的占位符。
     *
     * @param beanFactory 包含 Bean 定义的 BeanFactory 实例。
     * @param properties  加载的属性配置文件。
     */
    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            // 判断属性当中是否有占位符存在，如果有则进行替换
            resolvePropertyValues(beanDefinition, properties);
        }
    }

    /**
     * 解析并替换 Bean 定义属性中的占位符。
     *
     * @param beanDefinition Bean 定义。
     * @param properties     加载的属性配置文件。
     */
    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
            Object value = propertyValue.getValue();
            if (value instanceof String) {
                // TODO 仅简单支持一个占位符的格式
                String strVal = (String) value;
                StringBuffer buf = new StringBuffer(strVal);
                int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
                int endIndex = strVal.indexOf(PLACEHOLDER_SUFFIX);
                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    String propKey = strVal.substring(startIndex + 2, endIndex);
                    String propVal = properties.getProperty(propKey);
                    buf.replace(startIndex, endIndex + 1, propVal);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), buf.toString()));
                }
            }
        }
    }

    /**
     * 加载属性配置文件。
     *
     * @return 加载的属性配置文件。
     */
    public Properties loadProperties() {
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (IOException e) {
            throw new BeansException(e.getMessage(), e);
        }
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
