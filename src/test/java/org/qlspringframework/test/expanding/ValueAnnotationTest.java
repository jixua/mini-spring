package org.qlspringframework.test.expanding;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.bean.People;

/**
 * @author jixu
 * @title ValueAnnotationTest
 * @date 2025/5/31 19:45
 */
public class ValueAnnotationTest {

    @Test
    public void testValueAnnotation(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:value-annotation.xml");
        People people = applicationContext.getBean("people", People.class);
        Assert.assertEquals(people.getName(),"jixu");
        Assert.assertEquals(people.getSex(),"man");
    }
}
