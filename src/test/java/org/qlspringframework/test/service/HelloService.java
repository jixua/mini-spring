package org.qlspringframework.test.service;

import org.qlspringframework.stereotype.Component;

/**
 * @author jixu
 * @title HelloService
 * @date 2025/4/7 09:55
 */
@Component("helloService")
public class HelloService {

    public void say(){
        System.out.println("hello");
    }
}
