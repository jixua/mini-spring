package org.qlspringframework.aop;

/**
 * ClassFilter接口用于定义类过滤器，它帮助筛选出满足特定条件的类
 * 这个接口主要用于框架内部，以决定哪些类需要被处理或关注
 *
 * @author jixu
 * @date 2025/5/20 15:45
 */
public interface ClassFilter {

    /**
     * 判断目标类是否匹配过滤条件
     * 此方法由实现ClassFilter接口的类来具体实现，用于定义匹配逻辑
     *
     * @param clazz 待筛选的目标类
     * @return 如果目标类匹配过滤条件，则返回true；否则返回false
     */
    boolean matches(Class<?> clazz);
}
