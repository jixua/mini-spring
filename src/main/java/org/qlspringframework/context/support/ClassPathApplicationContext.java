package org.qlspringframework.context.support;

/**
 *
 *
 * @author: jixu
 * @create: 2025-04-21 17:23
 **/
public class ClassPathApplicationContext extends AbstractXmlApplicationContext{

    private String[] configLocations;

    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }


    public ClassPathApplicationContext(String configLocation){
        this.configLocations = new String[]{configLocation};
    }

    public ClassPathApplicationContext(String[] configLocations){
        this.configLocations = configLocations;
    }

}
