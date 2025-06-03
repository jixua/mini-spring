package org.qlspringframework.test.expanding;

import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.bean.People;

/**
 * @author jixu
 * @title AutowiredAnnotationTest
 * @date 2025/6/1 17:21
 */
public class AutowiredAnnotationTest {

    @Test
    public void testAutowiredAnnotation(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:autowired-annotation.xml");
        People people = applicationContext.getBean("people", People.class);
        people.getHelloService().say();
    }
}
