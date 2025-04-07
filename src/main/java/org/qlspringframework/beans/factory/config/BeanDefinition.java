package org.qlspringframework.beans.factory.config;

import org.qlspringframework.beans.PropertyValues;

/**
 * @description: Bean的定义信息
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




    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public BeanDefinition(Class beanClass){
        this(beanClass,null );
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues: new PropertyValues();
    }

    public BeanDefinition(String beanName, PropertyValues propertyValues) {
        this.beanName = beanName;
        this.propertyValues = propertyValues;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
