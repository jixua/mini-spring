package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.BeanException;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.factory.config.AutowireCapableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.beans.factory.config.BeanReference;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @description: 主要负责Bean的创建逻辑
 * @author: jixu
 * @create: 2025-03-28 16:42
 **/
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();



    /**
     * @param beanName Bean名称
     * @param beanDefinition Bean的定义信息
     * @return Bean实列
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        return doCreateBean(beanName , beanDefinition);
    }


    /**
     * 执行具体创建Bean的逻辑
     *
     * 如何创建Bean？
     * 通过beanDefinition当中保存的Bean的Class对象，通过反射的方式创建Bean
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeanException {


        // 通过反射创建对象
        Object bean = null;
        try {
            // 通过InstantiationStrategy实例化Bean
            bean = createBeanInstance(beanDefinition);

            // 为Bean的属性进行赋值
            applyPropertyValues(bean , beanDefinition , beanName);

            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName , bean);
        } catch (Exception e) {
            throw new BeanException(e.getMessage());
        }

        // 创建完毕后加入缓存
        super.addSingletonBean(beanName, bean);

        return bean;
    }

    private void initializeBean(String beanName, Object bean) {
        // 执行初始化之前的BeanPostProcessor前置处理
        Object wrappedBean  = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行初始化方法

        // 执行初始化之前的BeanPostProcessor后置
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean , beanName);

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
     * 根据BeanDefinition中的属性信息，为指定的bean对象应用属性值
     * 此方法主要用于依赖注入过程，通过反射机制将属性值注入到bean实例中
     *
     * @param bean 要应用属性值的目标bean对象
     * @param beanDefinition 包含bean定义和属性信息的对象
     * @param beanName bean的名称，用于错误信息或日志记录中
     */
    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition, String beanName) {
        try {
            // 获取到要操作Bean到Class对象
            Class beanClass = beanDefinition.getBeanClass();

            // 循环获取当前Bean的所有属性
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
                // 对于属性的赋值要通过对应的set方法，构造出set方法的方法名
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                // 如果是Bean
                if (value instanceof BeanReference){
                    value = super.getBean(name);
                }


                //通过属性的set方法设置属性
                String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Class<?> type = beanClass.getDeclaredField(name).getType();

                // 通过反射动态调用
                Method declaredMethod = beanClass.getDeclaredMethod(setMethodName, type);
                declaredMethod.invoke(bean, value);
                // BeanUtil.setFieldValue(bean, name, value);
            }
        }catch (Exception e){
            throw new BeanException(String.format("bean 属性注入异常[%s]",beanName) ,  e);
        }
    }


    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
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
