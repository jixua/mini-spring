package org.qlspringframework.aop.framework;

import org.qlspringframework.aop.AdvisedSupport;

/**
 * 代理工厂类
 *
 * 该类用于生成代理对象，根据配置的不同选择合适的代理方式
 * 主要支持两种代理方式：基于接口的JDK动态代理和基于字节码的CGLIB代理
 *
 * @author jixu
 * @title ProxyFactory
 * @date 2025/5/24 15:35
 */
public class ProxyFactory {

    // 保存代理配置信息的属性
    private final AdvisedSupport advisedSupport;

    /**
     * 构造方法，初始化代理工厂
     *
     * @param advisedSupport 包含代理配置信息的对象
     */
    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }


    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    /**
     * 获取代理对象的方法
     * 根据是否代理目标类本身来决定使用哪种代理方式
     * 如果是代理目标类本身，则使用CGLIB代理；否则使用JDK动态代理
     *
     * @return AopProxy接口的实现对象，具体类型取决于代理方式
     */
    public AopProxy createAopProxy(){
        // 根据配置信息判断是否需要代理目标类本身
        if (advisedSupport.isProxyTargetClass()){
            // 如果需要代理目标类本身，则返回CglibAopProxy代理对象
            return new CglibDynamicAopProxy(advisedSupport);
        }

        // 如果不需要代理目标类本身，则返回JdkDynamicAopProxy代理对象
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
