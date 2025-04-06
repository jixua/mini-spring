package org.qlspringframework.beans.factory.config;

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


    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
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
