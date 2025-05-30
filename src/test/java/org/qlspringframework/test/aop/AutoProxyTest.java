package org.qlspringframework.test.aop;

import org.junit.Test;
import org.qlspringframework.test.service.WorldService;
import org.qlspringframework.context.support.ClassPathXmlApplicationContext;

public class AutoProxyTest {

	@Test
	public void testAutoProxy() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:auto-proxy.xml");

		// 获取代理对象
		WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
		worldService.sayHello();
	}
}