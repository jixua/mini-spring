package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description: 管理 Bean 的生命周期（创建、依赖注入、缓存等）
 * @author: jixu
 * @create: 2025-03-28 17:06
 **/
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegister , ConfigurableListableBeanFactory {


    private Map<String , BeanDefinition> beanDefinitionMap = new HashMap<>();



    /**
     * 注册BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }


    /**
     * 获取BeanDefinition
     *
     * @param beanName
     * @return
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    /**
     *
     */
    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }


    /**
     * 根据指定的类型获取所有符合条件的Bean实例，并以Map形式返回。
     * Map的键为Bean的名称，值为对应的Bean实例。
     *
     * @param type 要查找的Bean类型
     * @return 包含所有符合类型条件的Bean实例的Map，键为Bean名称，值为Bean实例
     */
    @Override
    public <T> Map<String, T> getBeanOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        this.beanDefinitionMap.forEach((beanName , beanDefinition)-> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)){
                T bean = (T) getBean(beanName);
                result.put(beanName,bean);
            }
        }) ;
        return result;
    }

    /**
     * 获取当前BeanFactory中所有Bean定义的名称。
     *
     * @return 包含所有Bean定义名称的字符串数组
     */
    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanDefinitionNames = beanDefinitionMap.keySet();
        return beanDefinitionNames.toArray(new String[beanDefinitionNames.size()]);
    }
}
