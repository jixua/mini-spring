package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.config.SingletonBeanRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 单列Bean的注册实现类
 * @author: jixu
 * @create: 2025-03-28 15:41
 **/
public class DefaultSignletonBeanRegister implements SingletonBeanRegister {


    // 保存单列Bean的地方
    private Map<String , Object> singletonObjects = new ConcurrentHashMap<>();


    @Override
    public Object getSingletonBean(String beanName) {
        return singletonObjects.get(beanName);
    }
}
