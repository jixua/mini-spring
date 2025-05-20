package org.qlspringframework.beans.ioc;

import org.junit.Test;
import org.qlspringframework.beans.ioc.common.CustomEvent;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jixu
 * @title ApplicationEventTest
 * @date 2025/5/19 21:30
 */
public class ApplicationEventTest {

    @Test
    public void testApplicationRefreshEvent(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-event.xml");

    }

    @Test
    public void testApplicationCloseEvent(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-event.xml");
        applicationContext.registerShutdownHook();
    }

    @Test
    public void testApplicationCustomEvent(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-event.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));
    }


}
