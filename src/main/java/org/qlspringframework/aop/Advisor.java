package org.qlspringframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 定义一个Advisor接口，主要用于提供Advice对象
 * 该接口不涉及具体的实现细节，而是定义了一个获取Advice的抽象方法
 *
 * @author jixu
 * @title Advisor
 * @date 2025/5/27 21:02
 */
public interface Advisor {

    /**
     * 获取一个Advice对象
     *
     * @return 返回一个Advice对象，用于提供具体的建议或策略
     */
    Advice getAdvice();
}
