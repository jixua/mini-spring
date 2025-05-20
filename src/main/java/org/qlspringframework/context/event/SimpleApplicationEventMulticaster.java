package org.qlspringframework.context.event;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.context.ApplicationEvent;
import org.qlspringframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * SimpleApplicationEventMulticaster类用于处理应用事件的多播
 * 它继承自AbstractApplicationEventMulticaster，实现了事件多播的具体逻辑
 *
 * @author jixu
 * @title SimpleApplicationEventMulticaster
 * @date 2025/5/19 19:59
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){
        super.beanFactory = beanFactory;
    }

    /**
     * 广播指定的应用程序事件到所有已注册的监听器
     *
     * @param event 要广播的事件
     */
    @Override
    public void multicastEvent(ApplicationEvent event) {
        // 循环获取当前所有的事件监听者
        for (ApplicationListener<ApplicationEvent> applicationListener : super.applicationListeners) {
            if (supportsEvent(applicationListener,event)){
                // 监听到事件发布，执行对应逻辑
                applicationListener.onApplicationEvent(event);
            }
        }
    }

    /**
     * 判断指定的事件监听器是否支持处理给定的应用程序事件
     *
     * @param applicationListener 事件监听器
     * @param event 应用程序事件
     * @return 如果监听器支持处理事件，则返回true；否则返回false
     */
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener,ApplicationEvent event){
        // 获取到applicationListener实现的第一个接口
        Type type = applicationListener.getClass().getGenericInterfaces()[0];
        // 获取到接口当中的泛型参数类型 --》 具体的ApplicationEvent
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        // 获取到具体的ApplicationEvent对应的类型名称
        String className = actualTypeArgument.getTypeName();

        Class<?> eventClassName;

        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException(String.format("事件名称：【%s】错误",className));
        }

        // 判断当前Event与目标Event是否相同
        return eventClassName.isAssignableFrom(event.getClass());

    }
}

