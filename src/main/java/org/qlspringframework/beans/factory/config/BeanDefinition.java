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
    private Class aClass;


    public BeanDefinition(Class beanClass){
        this.aClass = beanClass;
    }


    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
