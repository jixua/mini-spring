package org.qlspringframework.test.expanding;

import org.junit.Test;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;
import org.qlspringframework.test.service.HelloService;

/**
 * @author jixu
 * @title PackageScanTest
 * @date 2025/5/31 15:32
 */
public class PackageScanTest {

    @Test
    public void testPackageScanTest(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:package-scan.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        helloService.say();
    }
}
