package org.qlspringframework.context.support;

/**
 * ClassPathXmlApplicationContext 是一个应用程序上下文的实现类，它从类路径下的配置文件中加载Bean定义。
 * 它继承自AbstractXmlApplicationContext，实现了配置位置的获取方法，并提供了构造函数来设置配置位置。
 *
 * @author: jixu
 * @create: 2025-04-21 17:23
 **/
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    // 存储配置文件的位置
    private String[] configLocations;

    /**
     * 获取配置文件位置的抽象方法的具体实现。
     *
     * @return 配置文件的位置数组。
     */
    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }

    /**
     * 单参数构造函数，用于设置单个配置文件位置。
     *
     * @param configLocation 配置文件的位置。
     */
    public ClassPathXmlApplicationContext(String configLocation){
        this.configLocations = new String[]{configLocation};
        super.refresh();
    }

    /**
     * 多参数构造函数，用于设置多个配置文件位置。
     *
     * @param configLocations 配置文件的位置数组。
     */
    public ClassPathXmlApplicationContext(String[] configLocations){
        this.configLocations = configLocations;
        super.refresh();
    }

}
