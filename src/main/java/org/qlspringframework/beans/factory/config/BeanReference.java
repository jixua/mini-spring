package org.qlspringframework.beans.factory.config;

/**
 * BeanReference类用于表示对Bean的引用
 * 它封装了Bean的名称，以便在Bean工厂中使用
 *
 * @author jixu
 * @title BeanReference
 * @date 2025/4/7 10:52
 */
public class BeanReference {
    private final String beanName;

    /**
     * 获取Bean的名称
     *
     * @return Bean的名称
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 构造函数，初始化BeanReference对象
     *
     * @param beanName Bean的名称，是将要引用的Bean的唯一标识
     */
    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
