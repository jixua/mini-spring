package org.qlspringframework.test.bean;

import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.BeanFactoryAware;

/**
 * @author jixu
 * @title Book
 * @date 2025/5/5 18:52
 */
public class Book implements BeanFactoryAware {

    private BeanFactory beanFactory;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
