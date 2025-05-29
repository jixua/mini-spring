
## 一、什么是 Advice（横切关注点）

参考：[AOP相关核心概念与作用说明（补充）](AOP相关核心概念与作用说明（补充）)


在 AOP（面向切面编程）中，`Advice` 是一个核心概念。其英文原意为：

> 建议、忠告、指引

在编程场景中，它表示一种“附加逻辑”：

> 在主业务方法执行的**前**、**后**或**异常**时，插入一段“建议性的”辅助操作，如记录日志、权限校验、事务控制等。

因此，Spring 与 AOP Alliance 将这种横切逻辑的实现统称为 `Advice`，即“通知”，它代表的是所有**可插入到业务流程中的切面逻辑**。

---

## 二、Advice 的实现方式

在 Spring AOP 中，所有通知类型都实现了 `org.aopalliance.aop.Advice` 接口，具体的通知则由其子接口定义。常见的通知类型如下：

| 通知类型 | 接口名称                   | 执行时机             | 注解形式              |
| ---- | ---------------------- | ---------------- | ----------------- |
| 前置通知 | `MethodBeforeAdvice`   | 方法执行之前           | `@Before`         |
| 后置通知 | `AfterReturningAdvice` | 方法成功返回之后         | `@AfterReturning` |
| 异常通知 | `ThrowsAdvice`（标记接口）   | 方法抛出异常后          | `@AfterThrowing`  |
| 最终通知 | —                      | 方法执行结束（无论是否异常）   | `@After`          |
| 环绕通知 | `MethodInterceptor`    | 方法执行前后（手动控制调用流程） | `@Around`         |

本文重点介绍：**前置通知（MethodBeforeAdvice）** 的实现。

---

## 三、相关接口与实现说明

#AfterAdvice：标记接口

`AfterAdvice` 是一个标记接口，本身不包含任何方法，仅用于标识该类型的通知。Spring 通过这些标记接口实现对不同 Advice 类型的统一识别与分类处理，实现通知逻辑的解耦与可扩展。

```java
/**
 * 前置通知标记接口（无具体方法，仅作分类用途）
 */
public interface AfterAdvice {
}
```

---

#MethodBeforeAdvice：前置通知接口

用于定义目标方法执行前的通知逻辑。

```java
public interface MethodBeforeAdvice extends BeforeAdvice {
    /**
     * 在目标方法调用前执行的操作
     *
     * @param method  被调用的方法
     * @param args    方法参数
     * @param target  目标对象
     * @throws Throwable 可抛出异常
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
```

---

#MethodBeforeAdviceInterceptor：前置通知拦截器实现类

该类将 `MethodBeforeAdvice` 封装为一个 `MethodInterceptor`，用于统一 AOP 拦截器调用流程。

```java
/**
 * 前置通知拦截器：在方法执行前调用指定的前置通知逻辑
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    // 注入的前置通知逻辑
    private final MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 在方法执行前调用前置通知逻辑
        advice.before(
            methodInvocation.getMethod(),
            methodInvocation.getArguments(),
            methodInvocation.getThis()
        );

        // 调用下一个拦截器或目标方法本身
        return methodInvocation.proceed();
    }
}
```

---


- `Advice` 是 AOP 中的关键接口，表示横切关注点的实际实现。
- Spring 通过不同子接口区分前置、后置、异常、环绕等多种通知类型。
- 通过拦截器封装通知逻辑，使其能灵活插入到方法执行链中，实现解耦和增强。


## 四、测试

```java
@Before  
public void setup(){  
	// 被代理对象
    WorldServiceImpl worldService = new WorldServiceImpl();  
    TargetSource targetSource = new TargetSource(worldService);  
    advisedSupper.setTargetSource(targetSource);  
  
    WorldServiceInterceptor interceptor = new WorldServiceInterceptor();  
    advisedSupper.setMethodInterceptor(interceptor);  
    // "execution(* org.springframework.test.service.WorldService.explode(..))"  
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.service.WorldService.sayHello(..))");  
    MethodMatcher methodMatcher = pointcut.getMethodMatcher();  
    advisedSupper.setMethodMatcher(methodMatcher);  
}


@Test  
public void testBeforeAdvice(){  
    WorldServiceBeforeAdvice advice = new WorldServiceBeforeAdvice();  
    MethodBeforeAdviceInterceptor methodInterceptor = new MethodBeforeAdviceInterceptor(advice);  
    advisedSupper.setMethodInterceptor(methodInterceptor);  
  
    CglibDynamicAopProxy cglibDynamicAopProxy = new CglibDynamicAopProxy(advisedSupper);  
    WorldService proxy = (WorldService) cglibDynamicAopProxy.getProxy();  
    proxy.sayHello();  
}
```