package org.qlspringframework.context.event;

import org.qlspringframework.context.ApplicationEvent;

/**
 * 该类是ApplicationContext事件的抽象基类，继承自ApplicationEvent
 * 它为所有的应用上下文事件提供了一个通用的父类，使得这些事件可以共享一些通用的行为和属性
 *
 * @author jixu
 * @title ApplicationContextEvent
 * @date 2025/5/19 17:18
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    /**
     * 构造一个原型事件对象
     *
     * @param source 事件最初发生的对象
     * @throws IllegalArgumentException 如果source为null，则抛出该异常
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * 获取事件源对象，即应用上下文事件的来源
     * 这个方法允许事件处理者获取到触发事件的上下文对象
     *
     * @return 返回事件源对象，即应用上下文事件的来源
     */
    public ApplicationContextEvent getApplicationContext() {
        return (ApplicationContextEvent) getSource();
    }
}
