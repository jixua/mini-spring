package org.qlspringframework.context.event;

/**
 * 事件刷新类
 *
 * @author jixu
 * @title ContextRefreshedEvent
 * @date 2025/5/19 17:24
 */
public class ContextRefreshedEvent extends ApplicationContextEvent{

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
