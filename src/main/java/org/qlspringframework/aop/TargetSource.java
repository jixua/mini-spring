package org.qlspringframework.aop;

/**
 * TargetSource类用于封装目标源对象，并提供获取目标对象及其接口信息的方法
 *
 * @author jixu
 * @title TargetSource
 * @date 2025/5/21 21:46
 */
public class TargetSource {

    // 保存目标源对象
    private final Object target;

    /**
     * 构造方法，用于创建TargetSource对象
     *
     * @param target 目标源对象
     */
    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * 获取被代理对象所实现的所有接口
     *
     * @return 目标对象所实现的接口数组
     */
    public Class<?>[] getTargetInterfaceClass(){
        return this.target.getClass().getInterfaces();
    }

    /**
     * 获取目标源对象
     *
     * @return 目标源对象
     */
    public Object getTarget(){
        return this.target;
    }
}

