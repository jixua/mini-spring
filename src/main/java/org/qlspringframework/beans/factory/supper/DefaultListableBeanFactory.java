package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;

import java.util.*;

/**
 * 默认的可列出Bean的工厂类，继承了AbstractAutowireCapableBeanFactory并实现了BeanDefinitionRegister和ConfigurableListableBeanFactory接口。
 * 该类主要负责Bean的定义、注册、获取等功能。
 *
 * @author: jixu
 * @create: 2025-03-28 17:06
 **/
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegister , ConfigurableListableBeanFactory {

    // 存储Bean名称和其对应定义的Map
    private Map<String , BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 注册BeanDefinition
     *
     * @param beanName Bean的名称
     * @param beanDefinition Bean的定义
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }



    /**
     * 获取BeanDefinition
     *
     * @param beanName Bean的名称
     * @return 对应名称的Bean定义
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    /**
     * 提前实例化所有单例Bean。
     */
    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.forEach((key,value) -> {
            if (value.isSingleton()){
                super.getBean(key);
            }
        });
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

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (beanNames.size() == 1) {
            return super.getBean(beanNames.get(0), requiredType);
        }

        throw new BeansException(requiredType + "expected single bean but found " +
                beanNames.size() + ": " + beanNames);
    }


    /**
     * 检查是否包含指定名称的Bean定义
     *
     * @param beanName 要检查的Bean名称
     * @return 如果包含指定名称的Bean定义则返回true，否则返回false
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }
}
