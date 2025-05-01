package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanFactoryPostProcessor;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.context.ConfigurableApplicationContext;
import org.qlspringframework.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * AbstractApplicationContext 是一个抽象类，实现了 ConfigurableApplicationContext 接口。
 * 该类提供了应用程序上下文的基本实现，特别是容器的刷新功能。
 *
 * @author: jixu
 * @create: 2025-04-19 14:37
 **/
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

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

        // 执行BeanFactoryPostProcess的方法
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册BeanPostPostProcess
        registerBeanPostProcessors(beanFactory);

        // 提前初始化单列Bean
        beanFactory.preInstantiateSingletons();

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
}

