package org.qlspringframework.context;

import org.qlspringframework.beans.BeanException;

/**
 * @description:
 * @author: jixu
 * @create: 2025-04-19 14:33
 **/
public class ApplicationContextException extends BeanException {

    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
