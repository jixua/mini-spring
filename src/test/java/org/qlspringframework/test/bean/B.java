package org.qlspringframework.test.bean;

import org.qlspringframework.beans.factory.annotation.Autowired;
import org.qlspringframework.stereotype.Component;

/**
 * @author jixu
 * @title B
 * @date 2025/6/6 19:08
 */
@Component
public class B {

    @Autowired
    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
