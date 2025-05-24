package org.qlspringframework.beans.common;

import org.qlspringframework.context.ApplicationListener;
import org.qlspringframework.context.event.ContextCloseEvent;

/**
 * @author jixu
 * @title ContextCloseEventListener
 * @date 2025/5/19 21:35
 */
public class ContextCloseEventListener implements ApplicationListener<ContextCloseEvent> {
    /**
     * 当有事件发布时，会触发该方法执行
     *
     * @param event 接收一个ApplicationEvent对象，表示发布的事件
     */
    @Override
    public void onApplicationEvent(ContextCloseEvent event) {
        System.out.println(this.getClass().getName());
    }
}
