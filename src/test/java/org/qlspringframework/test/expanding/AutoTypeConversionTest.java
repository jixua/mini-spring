package org.qlspringframework.test.expanding;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.bean.People;

/**
 * @author jixu
 * @title AutoTypeConversionTest
 * @date 2025/6/6 00:03
 */
public class AutoTypeConversionTest {

    @Test
    public void testConversion(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:type-conversion.xml");
        People people = applicationContext.getBean("people", People.class);
        Assert.assertEquals(people.getAge(),Integer.valueOf(10));
    }
}
