package org.qlspringframework.beans.ioc.common;

import org.qlspringframework.beans.factory.config.BeanPostProcessor;
import org.qlspringframework.beans.ioc.bean.People;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-15 11:21
 **/
public class CustomerBeanPostProcessor implements BeanPostProcessor {
    /**
     * 在 Bean 初始化之前执行自定义处理逻辑。
     *
     * @param bean     当前正在初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("people")) {
            System.out.println("CustomerBeanPostProcessor.postProcessBeforeInitialization()");
            People people = (People) bean;
            people.setName("customer");
            return people;
        }
        return bean;

    }

    /**
     * 在 Bean 初始化之后执行自定义处理逻辑。
     *
     * @param bean     当前已经初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }
}
