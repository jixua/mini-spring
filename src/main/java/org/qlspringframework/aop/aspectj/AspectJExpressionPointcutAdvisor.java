package org.qlspringframework.aop.aspectj;

import org.aopalliance.aop.Advice;
import org.qlspringframework.aop.PointCut;
import org.qlspringframework.aop.PointcutAdvisor;

/**
 * AspectJ表达式切点顾问类
 * 该类实现了PointcutAdvisor接口，用于提供切点表达式和建议对象
 * 主要功能包括设置和获取切点表达式、设置和获取建议对象
 *
 * @author jixu
 * @title AspectJExpressionPointcutAdvisor
 * @date 2025/5/27 21:07
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 存储建议对象的变量
    private Advice advice;

    // 存储切点对象的变量
    private PointCut pointcut;

    // 存储切点表达式的变量
    private String expression;

    // 默认构造函数
    public AspectJExpressionPointcutAdvisor() {
    }

    /**
     * 获取切点对象
     *
     * @return PointCut对象，表示切点
     */
    @Override
    public PointCut getPointcut() {
        return pointcut;
    }

    /**
     * 获取建议对象
     *
     * @return Advice对象，表示建议
     */
    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    /**
     * 设置建议对象
     *
     * @param advice Advice对象，表示要设置的建议
     */
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    /**
     * 设置切点表达式，并根据表达式创建切点对象
     * 当表达式被设置时，会创建一个新的AspectJExpressionPointcut对象
     *
     * @param expression 字符串，表示切点表达式
     */
    public void setExpression(String expression) {
        this.expression = expression;
        pointcut = new AspectJExpressionPointcut(expression);
    }
}
