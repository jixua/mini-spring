package org.qlspringframework.beans.factory;

import java.util.Map;
/**
 * ListableBeanFactory 接口扩展了 BeanFactory 接口，提供了对 Bean 的列举功能。
 * 该接口允许获取工厂中所有 Bean 的名称、根据类型获取 Bean 的名称或实例，
 * 以及检查 Bean 的定义信息等操作。
 *
 * 通过实现该接口，BeanFactory 可以提供更丰富的 Bean 管理功能，特别是在需要
 * 列举或查询 Bean 的场景下。
 *
 * @see BeanFactory
 */
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
    String[] getBeanDefinitionNames();
}
