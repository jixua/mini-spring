package org.qlspringframework.context.event;

import org.qlspringframework.context.ApplicationEvent;
import org.qlspringframework.context.ApplicationListener;

/**
 * 事件广播器接口，用于管理监听器的注册和事件的广播
 *
 * @author jixu
 * @title ApplicationEventMulticaster
 * @date 2025/5/19 19:44
 *
 */
public interface ApplicationEventMulticaster  {

    /**
     * 添加一个应用事件监听器
     *
     * @param listener 要添加的监听器
     */
    public void addApplicationListener(ApplicationListener<?> listener);


    /**
     * 移除一个应用事件监听器
     *
     * @param listener 要移除的监听器
     */
    public void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播指定的应用程序事件到所有已注册的监听器
     *
     * @param event 要广播的事件
     */
    public void multicastEvent(ApplicationEvent event);
}
