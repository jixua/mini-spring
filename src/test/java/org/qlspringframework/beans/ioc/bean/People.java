package org.qlspringframework.beans.ioc.bean;

import org.qlspringframework.beans.factory.DisposableBean;
import org.qlspringframework.beans.factory.InitializingBean;

/**
 * @author jixu
 * @title People
 * @date 2025/4/7 09:54
 */
public class People implements DisposableBean, InitializingBean {

    private String name;
    private Integer age;

    private Car car;

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
}
