package org.qlspringframework.aop.framework.autoproxy;



/**
 * DefaultAdvisorAutoProxyCreator类的作用是自动创建代理Bean，该类主要负责根据Advisor来创建代理Bean，
 * 并将这些Bean自动应用到应用程序上下文中。它实现了BeanPostProcessor接口，以便在Bean创建前后进行处理，
 * 并使用AopProxyFactory来创建代理对象。该类还支持通过设置暴露代理属性来控制是否将代理Bean暴露给其他Bean使用。
 *
 * @author jixu
 * @title DefaultAdvisorAutoProxyCreator
 * @date 2025/5/28 15:48
 */
public class DefaultAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator  {


}
