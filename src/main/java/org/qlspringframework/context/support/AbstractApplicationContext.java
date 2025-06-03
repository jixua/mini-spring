package org.qlspringframework.context.support;

import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanFactoryPostProcessor;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.context.ApplicationEvent;
import org.qlspringframework.context.ApplicationListener;
import org.qlspringframework.context.ConfigurableApplicationContext;
import org.qlspringframework.context.event.ApplicationEventMulticaster;
import org.qlspringframework.context.event.ContextCloseEvent;
import org.qlspringframework.context.event.ContextRefreshedEvent;
import org.qlspringframework.context.event.SimpleApplicationEventMulticaster;
import org.qlspringframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * AbstractApplicationContext 是一个抽象类，实现了 ConfigurableApplicationContext 接口。
 * 该类提供了应用程序上下文的基本实现，特别是容器的刷新功能。
 *
 * @author: jixu
 * @create: 2025-04-19 14:37
 **/
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    /**
     * 刷新容器。
     * 该方法用于重新加载或刷新应用程序上下文中的配置和资源。
     * 通常用于在运行时动态更新应用程序的配置。
     *
     * 该方法是一个抽象方法的实现，具体刷新逻辑由子类提供。
     */
    @Override
    public void refresh() {
        // 通过子类创建BeanFactory，同时初始化beanDefinition
        refreshBeanFactory();

        // 获取到Bean工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 对于ApplicationContextAware来说是作用在单个Bean当中的，其中ApplicationContext无法指定在哪个Bean当中生效
        // 在这里可以通过BeanPostProcessor实现，此时的ApplicationContextAwarePostProcessor类似于一个中间件，将对象存储在当中
        // 当PostProcessor接口识别到该类型的Bean则会将其注入进去
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 执行BeanFactoryPostProcess的方法
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册BeanPostPostProcess
        registerBeanPostProcessors(beanFactory);

        // 初始化事件发布器
        initApplicationEventMulticaster();

        // 注册事件监听器
        registerListeners();

        // 提前初始化单列Bean
        beanFactory.preInstantiateSingletons();

        // 发布容器刷新完成事件，通知实现了ContextRefreshedEvent
        finishRefresh();


    }





    /**
     * 初始化事件监听器applicationEventMulticaster
     */
    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingletonBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME,applicationEventMulticaster);
    }


    /**
     *  将ApplicationListener的子类事件监听者加入到对应容器当中
     */
    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeanOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            this.applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }



    /**
     * 发布容器刷新完成事件，通知实现了ContextRefreshedEvent
     */
    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }


    /**
     * 发布事件
     *
     * @param event 待发布的应用事件，不能为空
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        // 将事件委托给应用事件多路广播器进行广播
        applicationEventMulticaster.multicastEvent(event);
    }




    /**
     * 关闭ApplicationContext
     */
    @Override
    public void close() {
        doClose();
    }

    private void doClose() {
        // 发布容器关闭通知
        publishEvent(new ContextCloseEvent(this));

        // 销毁Bean
        destroyBeans();

    }

    private void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::doClose);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * 注册BeanPostProcessor。
     * 该方法从BeanFactory中获取所有BeanPostProcessor类型的Bean，并将它们添加到BeanFactory中。
     * 这样做是为了确保这些处理器能够在Bean的生命周期中被调用。
     *
     * @param beanFactory ConfigurableListableBeanFactory类型的参数，用于获取和添加BeanPostProcessor
     */
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeanOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 调用BeanFactoryPostProcessor的方法。
     * 该方法从BeanFactory中获取所有BeanFactoryPostProcessor类型的Bean，并调用它们的postProcessBeanFactory方法。
     * 这允许这些处理器在BeanFactory初始化后对BeanFactory进行进一步的处理。
     *
     * @param beanFactory ConfigurableListableBeanFactory类型的参数，用于获取BeanFactoryPostProcessor并调用其方法
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取到所有已注册到容器当中的BeanPostProcess
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeanOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }

    }


    /**
     * 抽象方法，由子类实现。
     * 用于刷新BeanFactory，包括重新加载Bean定义等操作。
     */
    protected abstract void refreshBeanFactory();

    /**
     * 抽象方法，由子类实现。
     * 用于获取当前应用程序上下文中使用的BeanFactory。
     *
     * @return 返回ConfigurableListableBeanFactory类型的BeanFactory实例
     */
    protected abstract ConfigurableListableBeanFactory  getBeanFactory();

    /**
     * 根据指定的类型获取所有符合条件的Bean实例，并以Map形式返回。
     * Map的键为Bean的名称，值为对应的Bean实例。
     *
     * @param type 要查找的Bean类型
     * @return 包含所有符合类型条件的Bean实例的Map，键为Bean名称，值为Bean实例
     */
    @Override
    public <T> Map<String, T> getBeanOfType(Class<T> type) {
        return getBeanFactory().getBeanOfType(type);
    }

    /**
     * 获取当前BeanFactory中所有Bean定义的名称。
     *
     * @return 包含所有Bean定义名称的字符串数组
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    /**
     * 根据指定的 Bean 名称获取对应的 Bean 实例。
     *
     * @param name Bean 的名称，通常是在 Spring 配置文件中定义的 Bean 的 ID 或名称。
     * @return 返回与指定名称对应的 Bean 实例。如果找不到对应的 Bean，可能会抛出异常。
     */
    @Override
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name,requiredType);
    }
}

