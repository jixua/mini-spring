package org.qlspringframework.test.ioc;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.test.bean.People;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title AwareTest
 * @date 2025/5/5 17:25
 */
public class AwareTest {

    @Test
    public void testBeanFactoryAware(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        People people = applicationContext.getBean("people", People.class);
        Assert.assertNotNull(people.getBeanFactory());
    }

    @Test
    public void testApplicationContextAware(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        People people = applicationContext.getBean("people", People.class);
        Assert.assertNotNull(people.getApplicationContext());
    }
}
