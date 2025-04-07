package org.qlspringframework.beans.factory.config;

/**
 * @author jixu
 * @title BeanReference
 * @date 2025/4/7 10:52
 */
public class BeanReference {
    private final String beanName;

    public String getBeanName() {
        return beanName;
    }

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
