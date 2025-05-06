package org.qlspringframework.context;

import org.qlspringframework.beans.factory.Aware;

/**
 * @author jixu
 * @title ApplicationContextAware
 * @date 2025/5/5 17:11
 */
public interface ApplicationContextAware extends Aware {

    public void setApplicationContext(ApplicationContext applicationContext);
}
