package org.qlspringframework.test.expanding;

import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.bean.People;

/**
 * @author jixu
 * @title AutoConversionTest
 * @date 2025/6/6 00:03
 */
public class AutoConversionTest {

    @Test
    public void testConversion(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:type-conversion.xml");
        People people = applicationContext.getBean("people", People.class);
        System.out.println(people.getAge());
    }
}
