package org.qlspringframework.beans.common;

import org.qlspringframework.beans.factory.FactoryBean;
import org.qlspringframework.beans.bean.Car;

/**
 * @author jixu
 * @title CarFactoryBean
 * @date 2025/5/20 14:13
 */
public class CarFactoryBean implements FactoryBean<Car> {
    @Override
    public Car getObject() {
        Car car = new Car();
        car.setName("aaa");
        return car;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
