package org.qlspringframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.qlspringframework.aop.ClassFilter;
import org.qlspringframework.aop.MethodMatcher;
import org.qlspringframework.aop.PointCut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jixu
 * @title AspectJExpressionPointcut
 * @date 2025/5/20 16:33
 */
public class AspectJExpressionPointcut implements ClassFilter , MethodMatcher , PointCut {


    // 定义支持的切入函数
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        // 加入支持的切入方法
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;


    // 通过构造函数传入具体的切点表达式，解析为PointcutExpression
    public AspectJExpressionPointcut(String expression) {
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }




    /**
     * 判断目标类是否匹配过滤条件
     * 此方法由实现ClassFilter接口的类来具体实现，用于定义匹配逻辑
     *
     * @param clazz 待筛选的目标类
     * @return 如果目标类匹配过滤条件，则返回true；否则返回false
     */
    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    /**
     * 判断给定的方法是否与当前上下文中的方法匹配。
     *
     * @param method      要检查的方法
     * @param targetClass 目标类，即方法所属的类
     * @return 如果方法匹配则返回true，否则返回false
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    /**
     * 获取类过滤器，用于判断哪些类需要应用通知
     *
     * @return ClassFilter 实例，用于过滤目标类
     */
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    /**
     * 获取方法匹配器，用于判断目标类中的哪些方法需要应用通知
     *
     * @return MethodMatcher 实例，用于匹配目标方法
     */
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
