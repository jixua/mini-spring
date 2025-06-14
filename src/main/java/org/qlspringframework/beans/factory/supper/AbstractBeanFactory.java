package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.FactoryBean;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.beans.factory.config.ConfigurableBeanFactory;
import org.qlspringframework.core.convert.ConversionService;
import org.qlspringframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象Bean工厂类，继承了DefaultSingletonBeanRegistry以支持单例Bean的注册和管理，
 * 同时实现了ConfigurableBeanFactory接口以支持配置和依赖注入功能。
 *
 * @author: jixu
 * @create: 2025-03-28 15:47
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * BeanPostProcess是对指定Bean的增强，可以定义多个processors
     */
    private final List<BeanPostProcessor> beanPostProcessors  = new ArrayList<>();

    /**
     * FactoryBean缓存列表，用于存储已创建好的FactoryBean对象
     */
    private final Map<String , Object> factoryBeanObjectCache = new HashMap<>();

    /**
     * 用于存储字符属性解析器对象，要解析的配置文件可能有多份，因此需要定义一个集合来存储
     */
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    /**
     * 类型转换器
     */
    private ConversionService conversionService;




    /**
     * 解析嵌入值，用于Value注解解析
     *
     * @param value
     * @return
     */
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : embeddedValueResolvers) {
            // 会判断传入字段是否包含属性占位符，如果包含则替换为配置文件当中的值
            result = resolver.resolveStringValue(result);
        }
        return result;

    }

    /**
     * 添加属性解析器
     *
     * @param stringValueResolver
     */
    @Override
    public void addEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        embeddedValueResolvers.add(stringValueResolver);
    }

    /**
     * 添加beanPostProcessor
     * @param beanPostProcessor 要添加的BeanPostProcessor实例
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 获取所有的BeanPostProcessor
     * @return 包含所有BeanPostProcessor的List
     */
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }

    /**
     * 获取Bean
     * 包含创建Bean的流程，在创建Bean的流程当中会先从缓存当中取，如果没有则创建
     * 在获取Bean之前需要获取到Bean的定义信息也就是BeanDefinition
     * 1，从缓存当中获取Bean
     * 2，尝试创建Bean并返回
     *
     * @param beanName Bean名称
     * @return 创建的Bean实例
     */
    @Override
    public Object getBean(String beanName) {
        // 尝试从缓存当中获取Bean
        Object sharedInstance = super.getSingletonBean(beanName);
        if (sharedInstance != null){
            return getObjectForBeanInstance(sharedInstance,beanName);
        }

        // 如果没有尝试创建Bean,Bean的创建需要通过BeanDefinition
        BeanDefinition beanDefinition = getBeanDefinition(beanName);

        if (beanDefinition == null){
            throw new BeansException("beanDefinition：【" + beanName + "】 为空");
        }

        // 创建Bean
        Object bean = createBean(beanName, beanDefinition);

        return getObjectForBeanInstance(bean,beanName);

    }
    /**
     * 根据Bean实例获取应返回的对象实例
     * 此方法主要用于处理FactoryBean的实例，以确保正确地获取对象
     *
     * @param beanInstance Bean实例，可能是FactoryBean实例
     * @param beanName Bean的名称，用于标识Bean
     * @return 返回的对象实例，可能是FactoryBean创建的对象，也可能是Bean实例本身
     */
    public Object getObjectForBeanInstance(Object beanInstance,String beanName){
        // 初始化对象为传入的Bean实例
        Object object = beanInstance;
        // 判断是否为FactoryBean实例
        if (beanInstance instanceof FactoryBean){
            // 强制转换为FactoryBean类型
            FactoryBean factoryBean = (FactoryBean) beanInstance;

            try {
                // 判断FactoryBean是否生成单例Bean
                if (factoryBean.isSingleton()){
                    // 尝试从FactoryBean对象缓存中获取对象
                    object = factoryBeanObjectCache.get(beanName);
                    // 如果缓存中不存在，则调用FactoryBean的getObject方法创建对象，并存入缓存
                    if (object == null){
                        object = factoryBean.getObject();
                        this.factoryBeanObjectCache.put(beanName,object);
                    }
                }else {
                    // 如果不是单例Bean，直接调用FactoryBean的getObject方法创建对象
                    object = factoryBean.getObject();
                }
            } catch (Exception e) {
                // 如果FactoryBean在创建对象时抛出异常，重新包装并抛出BeansException
                throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
            }
        }

        // 返回最终的对象实例
        return object;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return  ((T) getBean(name));

    }

    /**
     * 创建Bean的抽象方法，由子类实现具体逻辑
     *
     * @param beanName Bean名称
     * @param beanDefinition Bean定义信息
     * @return 创建的Bean实例
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    /**
     * 获取Bean定义信息的抽象方法，由子类实现具体逻辑
     *
     * @param beanName Bean名称
     * @return Bean定义信息
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) ;


    protected abstract boolean containsBeanDefinition(String name);


    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }




    public Map<String, Object> getFactoryBeanObjectCache() {
        return factoryBeanObjectCache;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }
}
