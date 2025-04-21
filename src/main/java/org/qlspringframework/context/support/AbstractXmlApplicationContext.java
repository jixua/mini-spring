package org.qlspringframework.context.support;

import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-21 17:19
 **/
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{


    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String[] locations = getConfigLocations();
        xmlBeanDefinitionReader.loadBeanDefinitions(locations);
    }

    // 获取到Classpath下的所有配置资源路径
    protected abstract String[] getConfigLocations();
}
