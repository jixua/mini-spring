package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.config.SingletonBeanRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 注册和管理单例 Bean 的实例
 * @author: jixu
 * @create: 2025-03-28 15:41
 **/
public class DefaultSignletonBeanRegister implements SingletonBeanRegister {


    // 保存单列Bean的地方
    private Map<String , Object> singletonObjects = new ConcurrentHashMap<>();


    /**
     * 获取单列Bean
     *
     * @param beanName Bean名称
     * @return Bean对象
     */
    @Override
    public Object getSingletonBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    /**
     * 添加单列Bean
     *
     * @param bean
     */
    @Override
    public void addSingletonBean(String baenName , Object bean) {
        this.singletonObjects.put(baenName , bean);
    }
}
