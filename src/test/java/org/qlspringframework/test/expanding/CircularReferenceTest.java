package org.qlspringframework.test.expanding;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.bean.A;
import org.qlspringframework.test.bean.B;

/**
 * @author jixu
 * @title CircularReferenceTest
 * @date 2025/6/6 19:10
 */
public class CircularReferenceTest {


    @Test
    public void testCircularReference(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:circular-reference-without-proxy-bean.xml");
        A a = applicationContext.getBean("a", A.class);
        B b = applicationContext.getBean("b", B.class);

        System.out.println(a);
        System.out.println(b.getA());
        Assert.assertEquals(b.getA(),a);


    }



}
