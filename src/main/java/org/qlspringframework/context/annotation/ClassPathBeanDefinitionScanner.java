package org.qlspringframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.supper.BeanDefinitionRegister;
import org.qlspringframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Set;
/**
 * 自定义的类路径Bean定义扫描器
 * 用于扫描指定包下的Bean候选组件，并注册到Bean定义注册表中
 * 主要功能包括解析Bean的作用域和名称
 *
 * @author jixu
 * @title ClassPathBeanDefinitionScanner
 * @date 2025/5/31 13:57
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    // 用于注册Bean定义的注册表
    private BeanDefinitionRegister register;

    /**
     * 构造方法，初始化Bean定义注册表
     *
     * @param register Bean定义注册表
     */
    public ClassPathBeanDefinitionScanner(BeanDefinitionRegister register) {
        this.register = register;
    }

    /**
     * 执行扫描操作，处理指定基础包下的所有候选组件
     *
     * @param basePackages 需要扫描的基础包数组
     */
    public void doScan(String... basePackages){
        for (String basePackage : basePackages) {
            // 找到指定基础包下所有的候选组件
            Set<BeanDefinition> candidates  = super.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {

                // 解析作用域
                String beanScope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(beanScope)){
                    candidate.setScope(beanScope);
                }

                // 解析Bean名称
                String beanName = determineBeanName(candidate);
                register.registerBeanDefinition(beanName,candidate);

            }

        }
    }

    /**
     * 确定Bean的名称
     * 如果Component注解有指定名称，则使用指定的名称；否则使用类名的首字母小写形式
     *
     * @param beanDefinition Bean定义
     * @return Bean的名称
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component annotation = beanClass.getAnnotation(Component.class);
        String value = annotation.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }

        return value;
    }

    /**
     * 解析Bean的作用域
     * 如果类上有Scope注解，则返回该注解的值；否则返回空字符串
     *
     * @param beanDefinition Bean定义
     * @return Bean的作用域
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {

        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null){
            return scope.value();
        }

        return StrUtil.EMPTY;
    }
}
