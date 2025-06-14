package org.qlspringframework.test.ioc;

import org.junit.Test;
import org.qlspringframework.beans.factory.supper.DefaultListableBeanFactory;
import org.qlspringframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.qlspringframework.test.bean.People;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-10 14:56
 **/
public class XmlResourceReaderTest {

    @Test
    public void testXmlResourceReader(){
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        People person = (People) factory.getBean("people");
        System.out.println(person);
    }
}
