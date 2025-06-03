package org.qlspringframework.test.bean;

import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.DisposableBean;
import org.qlspringframework.beans.factory.InitializingBean;
import org.qlspringframework.beans.factory.annotation.Autowired;
import org.qlspringframework.beans.factory.annotation.Value;
import org.qlspringframework.context.ApplicationContext;
import org.qlspringframework.context.ApplicationContextAware;
import org.qlspringframework.stereotype.Component;
import org.qlspringframework.test.service.HelloService;

/**
 * @author jixu
 * @title People
 * @date 2025/4/7 09:54
 */
@Component
public class People implements DisposableBean, InitializingBean, BeanFactoryAware , ApplicationContextAware {


    @Value("jixu")
    private String name;

    @Value("${sex}")
    private String sex;

    @Autowired
    private HelloService helloService;

    public HelloService getHelloService() {
        return helloService;
    }

    private Integer age;

    private Car car;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private BeanFactory beanFactory;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private ApplicationContext applicationContext;


    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + car +
                '}';
    }

    @Override
    public void destroy() {
        System.out.println("People destroy");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("People init");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
