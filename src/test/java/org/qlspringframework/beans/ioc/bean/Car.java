package org.qlspringframework.beans.ioc.bean;

/**
 * @author jixu
 * @title Car
 * @date 2025/4/7 10:59
 */
public class Car {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }
}
