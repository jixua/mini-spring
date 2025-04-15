package org.qlspringframework.beans.factory;

import java.util.Map;

/**
 * @description: 对BeanFactory的相关功能进行扩展
 * @author: jixu
 * @create: 2025-04-11 15:00
 **/
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 根据指定的类型获取所有符合条件的Bean实例，并以Map形式返回。
     * Map的键为Bean的名称，值为对应的Bean实例。
     *
     * @param type 要查找的Bean类型
     * @param <T>  Bean的泛型类型
     * @return 包含所有符合类型条件的Bean实例的Map，键为Bean名称，值为Bean实例
     */
    <T> Map<String, T> getBeanOfType(Class<T> type);

    /**
     * 获取当前BeanFactory中所有Bean定义的名称。
     *
     * @return 包含所有Bean定义名称的字符串数组
     */
    String[] getBeanDefinitionName();
}
