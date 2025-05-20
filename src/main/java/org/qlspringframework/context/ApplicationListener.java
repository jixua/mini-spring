package org.qlspringframework.context;

import java.util.EventListener;

/**
 * 应用事件监听接口，用于监听并处理应用中的事件传播
 *
 * @author jixu
 * @title ApplicationListener
 * @date 2025/5/19 19:40
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 当有事件发布时，会触发该方法执行
     *
     * @param event 接收一个ApplicationEvent对象，表示发布的事件
     */
    void onApplicationEvent(E event);
}
