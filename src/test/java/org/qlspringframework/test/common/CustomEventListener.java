package org.qlspringframework.test.common;

import org.qlspringframework.context.ApplicationListener;

/**
 * @author jixu
 * @title CustomEventListener
 * @date 2025/5/19 21:40
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {


    /**
     * 当有事件发布时，会触发该方法执行
     *
     * @param event 接收一个ApplicationEvent对象，表示发布的事件
     */
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(this.getClass().getName());
    }
}
