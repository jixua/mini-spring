package org.qlspringframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫描并返回所有Component注解的类，并返回其BeanDefinition对象
 *
 * 该类的主要作用是在指定的包路径下扫描所有被Component注解标记的类，
 * 并将这些类的相关信息封装到BeanDefinition对象中，最终以集合的形式返回这些对象
 *
 * @author jixu
 * @title ClassPathScanningCandidateComponentProvider
 * @date 2025/5/31 13:50
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 根据包名扫描并返回所有被Component注解的类的BeanDefinition集合
     *
     * 该方法通过调用ClassUtil工具类的scanPackageByAnnotation方法来获取所有被Component注解的类，
     * 然后遍历这些类，为每一个类创建一个BeanDefinition对象，并添加到返回的集合中
     *
     * @param basePackage 基础包名，扫描的根包
     * @return 包含所有被Component注解的类的BeanDefinition的集合
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage){
        // 初始化一个集合用于存储扫描到的BeanDefinition对象
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();

        // 使用ClassUtil工具类扫描指定包路径下所有被Component注解的类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        // 遍历扫描到的类集合
        for (Class<?> aClass : classes) {
            // 为每一个类创建一个BeanDefinition对象
            BeanDefinition beanDefinition = new BeanDefinition(aClass);
            // 将创建的BeanDefinition对象添加到候选集合中
            candidates.add(beanDefinition);
        }

        // 返回包含所有被Component注解的类的BeanDefinition的集合
        return candidates;

    }
}
