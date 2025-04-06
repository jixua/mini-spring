package org.qlspringframework.beans.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Bean工厂，提供最基础的Bean的get/set
 * @author: jixu
 * @create: 2025-03-28 15:23
 **/
public interface BeanFactory {



    /**
     * 注册Bean
     * @param name Bean名称
     * @param bean bean对象
     */
//    public void registerBean(String name , Object bean);


    /**
     * 获取Bean
     * @param name Bean名称
     */
    public Object getBean(String name);

}
