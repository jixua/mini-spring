package org.qlspringframework.beans.ioc;

import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title PrototypeBeanTest
 * @date 2025/5/7 22:24
 */
public class PrototypeBeanTest {

    @Test
    public void testPrototypeBean(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:prototype-bean.xml");
        Object people = applicationContext.getBean("people");
        // Object people1 = applicationContext.getBean("people");
        // Assert.assertNotEquals(people1,people);
    }
}
