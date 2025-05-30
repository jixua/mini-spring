package org.qlspringframework.test.ioc;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.test.bean.People;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author jixu
 * @title ApplicationContextTest
 * @date 2025/5/1 21:17
 */
public class ApplicationContextTest {

    @Test
    public void testApplicationContext(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        People people = (People) classPathXmlApplicationContext.getBean("people");
        String name = people.getName();
        Assert.assertThat(name,equalTo("customer"));
    }
}
