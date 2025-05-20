package org.qlspringframework.context.event;

import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.context.ApplicationEvent;
import org.qlspringframework.context.ApplicationListener;

import java.util.HashSet;
import java.util.Set;
/**
 * 抽象的应用程序事件多播器类，负责管理应用程序事件的广播
 * 它实现了ApplicationEventMulticaster和BeanFactoryAware接口
 *
 * @author jixu
 * @title AbstractApplicationEventMulticaster
 * @date 2025/5/19 19:45
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster , BeanFactoryAware {

    // 定义一个Set集合用于保存所有的事件监听者
    protected final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

    // 保存BeanFactory的引用，以便于访问BeanFactory中的资源
    protected BeanFactory beanFactory;

    /**
     * 设置BeanFactory引用
     *
     * @param beanFactory BeanFactory实例
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 添加应用程序事件监听器
     *
     * @param listener 要添加的事件监听器
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    /**
     * 移除应用程序事件监听器
     *
     * @param listener 要移除的事件监听器
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove((ApplicationListener<ApplicationEvent>) listener);
    }
}
