package org.qlspringframework.beans.ioc;

import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title InitAndDestoryMethodTest
 * @date 2025/5/3 23:50
 */
public class InitAndDestroyMethodTest {

    @Test
    public void testInitAndDestroy(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        classPathXmlApplicationContext.registerShutdownHook();
    }
}
