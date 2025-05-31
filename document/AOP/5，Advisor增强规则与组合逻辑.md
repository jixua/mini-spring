
## 一、什么是 Advisor？

参考：[AOP相关核心概念与作用说明（补充）](AOP相关核心概念与作用说明（补充）)


### 1. Advisor 的定义与本质

`Advisor`（顾问）是 Spring AOP 提供的一个接口，用于封装以下两部分内容：

- **Advice**：增强逻辑，即实际执行的横切代码。

- **Pointcut**：切点逻辑，定义增强逻辑在哪些连接点（方法）生效。


> 通俗来说，**Advisor = Advice + Pointcut**

这种设计让 Spring AOP 框架可以通过统一的方式处理各种通知类型（如前置、后置、环绕等）。

#### 接口定义：

```java
public interface Advisor {
    Advice getAdvice();
}
```

当一个 Advisor 同时包含切点信息时，会实现其子接口 `PointcutAdvisor`：

```java
public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut(); // 获取切点定义
}
```

---

### 2. Advisor 的核心作用

####  统一封装切点与通知

在 Spring AOP 中，大量组件实现了 `Advisor` 接口。通过这一统一抽象，框架无需区分通知类型（如 `BeforeAdvice` / `AfterAdvice`），可以统一处理逻辑。

####  构建拦截器链的基础

Spring AOP 在执行代理方法时会构建一个拦截器链（interceptor chain），每个 `Advisor` 都会被包装为 `MethodInterceptor` 插入到链中。只有切点匹配的 `Advisor` 才会生效。

#### 实现增强逻辑的灵活组合

多个 `Advisor` 可以作用于同一个目标类，实现增强逻辑的灵活组合和动态添加，具有良好的扩展性和可插拔性。

> 简而言之，`Advisor` 就是 Spring AOP 中的“顾问”，它告诉框架：  
> **在哪些地方执行什么增强逻辑**

---

## 二. Sprin当中的实现逻辑

#### 1 `Advisor` 接口定义

```java
/**
 * 顾问接口：封装一个 Advice（通知逻辑）
 * 不关注切点，适用于全局增强逻辑
 * 
 * @author jixu
 * @date 2025/5/27
 */
public interface Advisor {
    /**
     * 获取通知逻辑对象
     */
    Advice getAdvice();
}
```

---

#### 2 `PointcutAdvisor` 接口定义

```java
/**
 * 切点顾问接口：封装 Advice + Pointcut
 * 常用于需要指定增强位置的通知（如方法级别拦截）
 * 
 * @author jixu
 * @date 2025/5/27
 */
public interface PointcutAdvisor extends Advisor {
    /**
     * 获取切点定义
     */
    PointCut getPointcut();
}
```

---

#### 3 `AspectJExpressionPointcutAdvisor` 实现类

```java
/**
 * 基于 AspectJ 表达式的切点顾问实现
 * 封装了一个切点表达式（Pointcut）和对应的通知（Advice）
 * 
 * 用于将 Pointcut + Advice 组合成统一 Advisor 结构
 * 
 * @author jixu
 * @date 2025/5/27
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private Advice advice;            // 通知逻辑
    private PointCut pointcut;        // 切点对象
    private String expression;        // 切点表达式

    public AspectJExpressionPointcutAdvisor() {}

    @Override
    public PointCut getPointcut() {
        if (this.pointcut == null) {
            this.pointcut = new AspectJExpressionPointcut(expression);
        }
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
```



|组件|作用|
|---|---|
|`Advice`|具体的增强逻辑，如日志打印、事务控制|
|`Pointcut`|定义哪些连接点需要增强|
|`Advisor`|封装 Advice 和 Pointcut，统一 AOP 调度入口|
|`PointcutAdvisor`|可匹配特定方法的 Advisor|
|`AspectJExpressionPointcutAdvisor`|结合 AspectJ 表达式生成动态切点|

通过将 Advice 与 Pointcut 封装为 Advisor，Spring AOP 实现了 **增强逻辑的统一管理、动态匹配与链式组合**，是整个框架的重要基础构件。
