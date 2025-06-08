package org.qlspringframework.test.bean;

import org.qlspringframework.beans.factory.annotation.Autowired;
import org.qlspringframework.stereotype.Component;

/**
 * @author jixu
 * @title A
 * @date 2025/6/6 19:08
 */
@Component
public class A {

    @Autowired
    private B b;

    public void func(){}


    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
