package org.qlspringframework.context;

import java.util.EventObject;

/**
 * ApplicationEvent 类是所有应用事件的基类。
 * 它继承自 EventObject 类，并在构造函数中要求提供事件的源对象。
 * 这个类是抽象的，意味着它不能直接被实例化，只能被子类继承。
 *
 * @author jixu
 * @title ApplicationEvent
 * @date 2025/5/19 17:14
 */
public abstract class ApplicationEvent extends EventObject {
    /**
     * 构造一个原型事件。
     *
     * @param source 事件最初发生的对象。
     * @throws IllegalArgumentException 如果 source 为 null，则抛出此异常。
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
