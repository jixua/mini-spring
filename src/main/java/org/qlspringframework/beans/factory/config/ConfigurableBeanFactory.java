package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.HierarchicalBeanFactory;
import org.qlspringframework.util.StringValueResolver;

/**
 * ConfigurableBeanFactory 接口扩展了 BeanFactory 和 SingletonBeanRegister 接口，
 * 提供了对 Bean 工厂的配置能力，特别是允许添加 BeanPostProcessor。
 *
 * @author jixu
 * @create 2025-04-11 15:42
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 添加一个 BeanPostProcessor 到工厂中。
     * BeanPostProcessor 可以在 Bean 初始化前后执行自定义逻辑。
     *
     * @param beanPostProcessor 要添加的 BeanPostProcessor 实例
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


    /**
     * 销毁单例bean，释放内存资源
     */
    void destroySingletons();


    /**
     * 解析嵌入值，用于Value注解解析
     * @param value 待解析的字符串，可能包含占位符
     * @return 解析后的字符串，占位符被实际值替换
     */
    String resolveEmbeddedValue(String value);


    /**
     * 添加属性解析器，以便在解析嵌入值时使用
     * @param stringValueResolver 属性解析器，用于解析字符串中的占位符
     */
    void addEmbeddedValueResolver(StringValueResolver stringValueResolver);



}
