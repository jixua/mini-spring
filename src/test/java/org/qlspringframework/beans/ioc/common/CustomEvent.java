package org.qlspringframework.beans.ioc.common;

import org.qlspringframework.context.ApplicationEvent;

/**
 * @author jixu
 * @title CustomEvent
 * @date 2025/5/19 21:39
 */
public class CustomEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomEvent(Object source) {
        super(source);
    }
}
