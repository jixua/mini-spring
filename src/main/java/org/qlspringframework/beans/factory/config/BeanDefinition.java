package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.PropertyValues;

/**
 * Bean定义类，封装了Bean的元数据信息
 * 包括Bean的名称、类对象以及属性列表等信息
 *
 * @author: jixu
 * @create: 2025-03-28 15:38
 **/
public class BeanDefinition {

    // Bean名称
    private String beanName;

    // Bean的Class对象
    private Class beanClass;

    // Bean属性列表
    private PropertyValues propertyValues;

    /**
     * 获取Bean的属性列表
     *
     * @return Bean的属性列表
     */
    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    /**
     * 设置Bean的属性列表
     *
     * @param propertyValues Bean的属性列表
     */
    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    /**
     * 构造函数，根据Bean的Class对象初始化BeanDefinition
     *
     * @param beanClass Bean的Class对象
     */
    public BeanDefinition(Class beanClass){
        this(beanClass,null );
    }

    /**
     * 构造函数，根据Bean的Class对象和属性列表初始化BeanDefinition
     *
     * @param beanClass Bean的Class对象
     * @param propertyValues Bean的属性列表
     */
    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues: new PropertyValues();
    }

    /**
     * 构造函数，根据Bean名称和属性列表初始化BeanDefinition
     *
     * @param beanName Bean名称
     * @param propertyValues Bean的属性列表
     */
    public BeanDefinition(String beanName, PropertyValues propertyValues) {
        this.beanName = beanName;
        this.propertyValues = propertyValues;
    }

    /**
     * 获取Bean的Class对象
     *
     * @return Bean的Class对象
     */
    public Class getBeanClass() {
        return beanClass;
    }

    /**
     * 设置Bean的Class对象
     *
     * @param beanClass Bean的Class对象
     */
    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 获取Bean名称
     *
     * @return Bean名称
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 设置Bean名称
     *
     * @param beanName Bean名称
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
