package org.qlspringframework.context.event;

/**
 * 事件关闭类
 *
 * @author jixu
 * @title ContextCloseEvent
 * @date 2025/5/19 17:23
 */
public class ContextCloseEvent extends ApplicationContextEvent{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextCloseEvent(Object source) {
        super(source);
    }
}
