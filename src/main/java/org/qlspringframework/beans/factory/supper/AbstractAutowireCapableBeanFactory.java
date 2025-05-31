package org.qlspringframework.beans.factory.supper;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.DisposableBean;
import org.qlspringframework.beans.factory.InitializingBean;
import org.qlspringframework.beans.factory.config.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * 抽象的自动装配功能的Bean工厂类
 * 实现了Bean的创建、依赖注入、初始化以及BeanPostProcessor的处理等功能
 *
 * @author: jixu
 * @create: 2025-03-28 16:42
 **/
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    // 实例化策略，用于创建Bean实例
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * 创建Bean实例
     * 根据Bean的定义信息，创建并初始化Bean实例
     *
     * @param beanName Bean名称
     * @param beanDefinition Bean的定义信息
     * @return Bean实例
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {

        try {
            // 给 BeanPostProcessors 一个返回代理而不是目标 bean 实例的机会。
            Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (bean != null) {
                return bean;
            }
        } catch (Throwable ex) {
            throw new BeansException("BeanPostProcessor before instantiation of bean failed", ex);
        }


        return doCreateBean(beanName , beanDefinition);
    }

    /**
     * 在实例化之前解析Bean
     * 如果Bean是代理对象，提前执行beanPostProcessor逻辑
     *
     * @param beanName Bean名称
     * @param beanDefinition Bean的定义信息
     * @return 如果是代理对象，则返回处理后的Bean实例，否则返回null
     */
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        // 判断当前Bean是否为代理对象，提前执行beanPostProcessor逻辑
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (bean != null){
            bean = applyBeanPostProcessorsAfterInitialization(bean,beanName);
        }
        return bean;
    }

    /**
     * 执行具体创建Bean的逻辑
     * 如何创建Bean？
     * 通过beanDefinition当中保存的Bean的Class对象，通过反射的方式创建Bean
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        // 通过反射创建对象
        Object bean = null;
        try {
            // 通过InstantiationStrategy实例化Bean
            bean = createBeanInstance(beanDefinition);

            applyBeanPostprocessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

            // 为Bean的属性进行赋值
            applyPropertyValues(bean , beanDefinition , beanName);

            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            bean = initializeBean(beanName , bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException(e.getMessage(),e);
        }
        // 注册带有销毁方法的Bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 创建完毕后加入缓存
        if (beanDefinition.isSingleton()){
            super.addSingletonBean(beanName, bean);
        }
        return bean;
    }



    private void applyBeanPostprocessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof  InstantiationAwareBeanPostProcessor) {
                PropertyValues propertyValues = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (propertyValues != null) {
                    for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }

            }
        }
    }

    /**
     * 如果需要，注册可销毁的Bean
     *
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     */
    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (beanDefinition.isSingleton()){
            if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())){
                super.registerDisposableBean(beanName,new DisposableBeanAdapter(bean,beanName, beanDefinition.getDestroyMethodName()));
            }
        }
    }

    /**
     * 初始化Bean
     * 执行Bean的初始化方法以及BeanPostProcessor的前置和后置处理方法
     *
     * @param beanName Bean名称
     * @param bean Bean实例
     */
    private Object initializeBean(String beanName, Object bean,BeanDefinition beanDefinition) {
        // 判断是否实现BeanFactoryAware接口，
        if (bean instanceof BeanFactoryAware){
            ((BeanFactoryAware)bean).setBeanFactory(this);
        }

        // 执行初始化之前的BeanPostProcessor前置处理
        Object wrappedBean  = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行初始化方法
        try {
            invokeInitMethods(beanName , wrappedBean , beanDefinition);
        } catch (Exception e) {
            throw new BeansException("调用 bean 的 init 方法[" + beanName + "] 失败", e);
        }

        // 执行初始化之前的BeanPostProcessor后置
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean , beanName);

        return wrappedBean;
    }

    /**
     * 调用初始化方法
     *
     * @param beanName Bean名称
     * @param bean Bean实例
     * @param beanDefinition Bean的定义信息
     * @throws Exception 如果初始化方法调用失败
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception{
        if (bean instanceof InitializingBean){
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 处理用户自定义的初始化方法
        if (StrUtil.isNotEmpty(beanDefinition.getInitMethodName())){
            Method initMethod = ClassUtil.getPublicMethod(bean.getClass(), beanDefinition.getInitMethodName());
            if (initMethod == null) {
                throw new BeansException(String.format("在Bean：%s 当中找不到名为：%s 的初始化方法",beanName,beanDefinition.getInitMethodName()));
            }
            initMethod.invoke(bean);
        }
    }

    /**
     * 创建并返回一个Bean实例
     * 此方法根据Bean定义来实例化Bean，具体实例化策略由获取到的实例化策略决定
     *
     * @param beanDefinition Bean的定义，包含了创建Bean实例所需的信息
     * @return 实例化的Bean对象
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    /**
     * 应用属性值
     * 根据BeanDefinition中的属性信息，为指定的bean对象应用属性值
     *
     * @param bean 要应用属性值的目标bean对象
     * @param beanDefinition 包含bean定义和属性信息的对象
     * @param beanName bean的名称，用于错误信息或日志记录中
     */
    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition, String beanName) {
        try {
            // 获取要操作Bean的Class对象
            Class beanClass = beanDefinition.getBeanClass();

            // 循环获取当前Bean的所有属性
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
                // 对于属性的赋值要通过对应的set方法，构造出set方法的方法名
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                // 如果是Bean引用，则获取对应的Bean实例
                if (value instanceof BeanReference){
                    BeanReference beanReference = (BeanReference) value;
                    value = super.getBean(beanReference.getBeanName());
                }

                // 通过属性的set方法设置属性
                String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Class<?> type = beanClass.getDeclaredField(name).getType();

                // 通过反射动态调用
                Method declaredMethod = beanClass.getDeclaredMethod(setMethodName, type);
                declaredMethod.invoke(bean, value);
            }
        }catch (Exception e){
            throw new BeansException(String.format("bean 属性注入异常[%s]",beanName) ,  e);
        }
    }

    // 获取实例化策略
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    // 设置实例化策略
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * 在实例化之前应用BeanPostProcessor
     *
     * @param beanClass Bean的类
     * @param beanName Bean的名称
     * @return 如果Bean是代理对象，则返回处理后的Bean实例，否则返回null
     */
    private Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        // 获取到所有的BeanPostProcess
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();

        // 筛选出InstantiationAwareBeanPostProcessor类型的beanPostProcessor
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {

            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                // 如果前置增强执行成功返回到的Bean非空则说明该Bean是被代理Bean
                Object bean = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (bean != null){
                    return bean;
                }

            }
        }

        return null;
    }

    /**
     * 在Bean初始化之前执行BeanPostProcessors的增强方法
     *
     * @param existingBean 当前已经存在的Bean实例
     * @param beanName     Bean的名称
     * @return 经过所有BeanPostProcessors处理后的Bean实例
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;

        // 获取到所有加入容器当中的BeanPostProcess
        List<BeanPostProcessor> beanPostProcessors = super.getBeanPostProcessors();
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            Object current = postProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 在Bean初始化之后执行BeanPostProcessors增强方法
     *
     * @param existingBean 当前已经存在的Bean实例
     * @param beanName     Bean的名称
     * @return 经过所有BeanPostProcessors处理后的Bean实例
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        List<BeanPostProcessor> beanPostProcessors = super.getBeanPostProcessors();
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            Object current = postProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null){
                return result;
            }
            result = current;
        }
        return result;
    }
}
