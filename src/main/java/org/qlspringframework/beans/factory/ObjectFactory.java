package org.qlspringframework.beans.factory;

/**
 * ObjectFactory接口用于定义对象工厂的标准行为。
 * 它代表了一个可以创建特定类型对象的工厂模型。
 * 使用泛型<T>允许在实例化时指定具体的对象类型。
 *
 * @author jixu
 * @title ObjectFactory
 * @date 2025/6/7 16:32
 */
public interface ObjectFactory <T>{

    /**
     * 创建并返回一个泛型类型T的对象。
     * 这个方法抽象化了对象创建的过程，使得调用者可以无需关心对象的具体创建细节。
     *
     * @return T类型的新创建对象。
     */
    T getObject();
}
