
package org.qlspringframework.context;

/**
 * ApplicationEventPublisher接口用于发布应用事件
 * 它允许将特定的事件通知给所有感兴趣的监听器
 * 主要用于实现应用内部的事件驱动架构
 *
 * @author jixu
 * @title ApplicationEventPublisher
 * @date 2025/5/19 20:59
 */
public interface ApplicationEventPublisher {

    /**
     * 发布一个应用事件
     *
     * @param event 要发布的应用事件实例，不能为空，该事件将被传递给所有注册的监听器
     */
    void publishEvent(ApplicationEvent event);
}
