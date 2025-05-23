在聊AOP之前，我们需要明白AOP是什么，以及在Spring当中的实现

参考：
- [Java 动态代理详解](https://juejin.cn/post/6844903744954433544)
- [从根上理解Cglib与JDK动态代理](https://juejin.cn/post/7172119902675796005)
- [深入理解Spring-AOP原理](https://mrbird.cc/%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3Spring-AOP%E5%8E%9F%E7%90%86.html)



Spring AOP是利用的动态代理机制，如果一个Bean实现了接口，那么就会采用JDK动态代理来生成该接口的代理对象，如果一个Bean没有实现接口，那么就会采用CGLIB来生成当前类的一个代理对象。代理对象的作用就是代理原本的Bean对象，代理对象在执行某个方法时，会在该方法的基础上增加一些切面逻辑，使得我们可以利用AOP来实现一些诸如登录校验、权限控制、日志记录等统一功能。

Spring AOP和Aspect之间并没有特别强的关系，AOP表示面向切面编程，这是一种思想，各个组织和个人都可以通过技术来实现这种思想，AspectJ就是其中之一，它会在编译期来对类进行增强，所以要用Aspect，得用Aspect开发的编译器来编泽你的项目。而Spring AOP则是米用动态代理的方式来实现AOP，只不过觉得Aspect中设计的那几个注解比较好，比如@Before、@After、@Around等，同时也不给程序员造成困扰，所以Spring AOP中会对这几个注解进行支持，虽然注解是相同的，但是底层的支持实现是完全不一样的。


# 一，JDK动态代理

在介绍动态代理的实现逻辑之前先聊一聊相关的接口与方法

让我来详细介绍 `MethodInvocation`、`MethodInterceptor` 和 `InvocationHandler` 之间的概念、作用以及它们在 Java 和 Spring AOP 中的关系。这三个类/接口在动态代理和 AOP（面向切面编程）中扮演了重要角色，但它们的用途和职责有所不同。


## 1，JDK动态代理的实现逻辑

参考：

[Java JDK 动态代理（AOP）使用及实现原理分析](https://blog.csdn.net/jiankunking/article/details/52143504)


首先在JDK动态代理当中有一个核心类Proxy，其中定义了静态方法newProxyInstance，通过该方法就可以为指定类创建代理对象

```java
public static Object newProxyInstance(ClassLoader loader,  
                                      Class<?>[] interfaces,  
                                      InvocationHandler h)
```

之后在我们调用该代理对象的对应方法时，VM 将调用转发到我们所自定义的InvocationHandler 的 invoke 方法。

参考一下内容

```java
public interface HelloService {
    void sayHello(String name);
}

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}

public class MyInvocationHandler implements InvocationHandler {
    private final Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("After: " + method.getName());
        return result;
    }
}

public static void main(String[] args) {
    HelloService target = new HelloServiceImpl();
    InvocationHandler handler = new MyInvocationHandler(target);
    HelloService proxy = (HelloService) Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        handler
    );
    proxy.sayHello("Alice");
}
```

现在我们通过Proxy.newProxyInstance创建被代理类的代理对象

```java
HelloService proxy = (HelloService) Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        handler
    );
```

调用被代理对象的proxy.sayHello("Alice");方法，由于该类时代理类，此时我们对其方法的调用JDK生成的最终真正的代理类，它继承自Proxy并实现了我们定义的Subject接口，  在实现Subject接口方法的内部，通过反射调用了MyInvocationHandler的invoke方法。
在MyInvocationHandler当中就会完成对代理类的增强逻辑 




## 2，Spring 是如何实现JDK动态代理的

### 2.1，底层原理

前面我们也简单介绍了一下JDK动态代理的使用与实现逻辑，现在我们再来思考一个问题，在原生的JDK动态代理当中我们所实现的`MyInvocationHandler`方法只能对指定类型的类进行增强，如果现在我们有多个业务，需要记录日志，幂等性校验等，那是否我们就需要实现多个`MyInvocationHandler`?那么多个`MyInvocationHandler`的执行顺序又如何处理？

在Spring当中为了解决这部分问题，通过`MethodInterceptor`构造拦截器责任链来实现的，至于责任链的实现我们暂时不做处理，等后续小节再去实现，在这里先看一下代码实现逻辑。


#ReflectiveMethodInvocation

ReflectiveMethodInvocation是一个方法拦截器，其中定义了调用方法，目标对象以及参数等成员变量，其实际上应该是一个组合好的拦截器执行链，它封装了所有需要在该方法上执行的横切关注点逻辑，为后续的链式调用提供了统一的入口。但在这里目前不做过多的实现，仅仅实现方法拦截以及代理增强的效果即可。

```java
  
/**  
 * ReflectiveMethodInvocation类实现了MethodInvocation接口，用于处理通过反射调用的方法  
 * 它封装了方法调用的相关信息，如方法本身、目标对象以及方法参数  
 *  
 * @author jixu  
 * @title ReflectiveMethodInvocation  
 * @date 2025/5/21 21:52  
 */
public class ReflectiveMethodInvocation implements MethodInvocation {  
  
    // 方法对象，表示要调用的方法  
    private final Method method;  
  
    // 目标对象，即要调用方法的实例  
    private final Object target;  
  
    // 方法参数数组，存储调用方法时传递的参数  
    private final Object[] argument;  
  
    /**  
     * 构造方法，初始化ReflectiveMethodInvocation对象  
     *  
     * @param method 方法对象，表示要调用的方法  
     * @param target 目标对象，即要调用方法的实例  
     * @param argument 方法参数数组，存储调用方法时传递的参数  
     */  
    public ReflectiveMethodInvocation(Method method, Object target, Object[] argument) {  
        this.method = method;  
        this.target = target;  
        this.argument = argument;  
    }  
  
    /**  
     * 获取要调用的方法  
     *  
     * @return Method对象，表示要调用的方法  
     */  
    @Override  
    public Method getMethod() {  
        return this.method;  
    }  
  
    /**  
     * 获取调用方法时传递的参数  
     *  
     * @return Object数组，包含调用方法时传递的参数  
     */  
    @Override  
    public Object[] getArguments() {  
        return this.argument;  
    }  
  
    /**  
     * 执行调用的方法  
     * 通过反射机制，使用目标对象和参数调用方法  
     *  
     * @return 方法调用的结果  
     * @throws Throwable 如果方法调用过程中发生异常  
     */  
    @Override  
    public Object proceed() throws Throwable {  
        return method.invoke(target, argument);  
    }  
  
    /**  
     * 获取当前调用方法的目标对象  
     *  
     * @return Object，表示当前调用方法的目标对象  
     */  
    @Override  
    public Object getThis() {  
        return target;  
    }  
  
    /**  
     * 获取当前调用方法的静态部分，即Method对象本身  
     *  
     * @return AccessibleObject，表示当前调用方法的静态部分  
     */  
    @Override  
    public AccessibleObject getStaticPart() {  
        return method;  
    }  
}
```

#AopProxy

AopProxy作为Spring AOP的核心接口，提供了获取代理对象的方法

```java
/**  
 * AopProxy接口定义了获取代理对象的方法  
 * 代理对象可以是JDK动态代理、CGLIB代理或其他类型的代理对象  
 * 主要用于在AOP（面向切面编程）中创建和管理代理对象  
 *  
 * @author jixu  
 * @title AopProxy  
 * @date 2025/5/21 21:51  
 */
public interface AopProxy {  
  
    /**  
     * 获取代理对象  
     *  
     * @return 代理对象，通过该对象可以调用目标方法以及切面方法  
     */  
    Object getProxy();  
}
```

#TargetSource

TargetSource是用于封装被代理对象信息的实体类

```java
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
    public Class<?>[] getTargetClass(){
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

```

#AdvisedSupper

AdvisedSupper类是用于配置和管理目标源、方法匹配器和方法拦截器


```java
/**
 * AdvisedSupper类是用于配置和管理目标源、方法匹配器和方法拦截器的超级类
 * 它提供了一种机制，用于在满足特定条件时，对目标源的方法调用进行拦截和处理
 * 
 * @author jixu
 * @title AdvisedSupper
 * @date 2025/5/21 21:49
 */
public class AdvisedSupper {

    // 目标源，代表了需要被拦截和处理的目标对象
    private TargetSource targetSource;

    // 方法匹配器，用于判断是否需要对某个方法进行拦截
    private MethodMatcher methodMatcher;

    // 方法拦截器，用于在满足条件时对方法进行实际的拦截和处理
    private MethodInterceptor methodInterceptor;

    /**
     * 获取方法匹配器
     * 
     * @return 当前的MethodMatcher实例
     */
    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    /**
     * 设置方法匹配器
     * 
     * @param methodMatcher 要设置的MethodMatcher实例，用于匹配方法
     */
    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    /**
     * 获取目标源
     * 
     * @return 当前的TargetSource实例
     */
    public TargetSource getTargetSource() {
        return targetSource;
    }

    /**
     * 设置目标源
     * 
     * @param targetSource 要设置的TargetSource实例，代表需要被处理的目标对象
     */
    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    /**
     * 获取方法拦截器
     * 
     * @return 当前的MethodInterceptor实例
     */
    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    /**
     * 设置方法拦截器
     * 
     * @param methodInterceptor 要设置的MethodInterceptor实例，用于拦截和处理方法调用
     */
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }
}

```

#JdkDynamicAopProxy

JdkDynamicAopProxy类实现了AopProxy和InvocationHandler接口

通过实现getProxy方法获取目标对象的代理类

```java
public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupper.getTargetSource().getTargetClass(), this);
    }
```

实现InvocationHandler的invoke方法，在调用代理类的方法时通过JVM生成的代理对象调用该invoke方法。同时在invoke当中通过所解析的切点表达式判断是否为被代理方法

```java
if (advisedSupper.getMethodMatcher().matches(method,advisedSupper.getTargetSource().getTarget().getClass())){
```

如果是获取到对应的方法拦截器，调用方法拦截器的invoke方法执行具体增强的代码逻辑

```java
/**
 * JdkDynamicAopProxy类实现了AopProxy和InvocationHandler接口，
 * 用于通过Java动态代理机制创建代理对象并处理方法调用。
 * 
 * @author jixu
 * @title JdkDynamicAopProxy
 * @date 2025/5/21 21:52
 */
public class JdkDynamicAopProxy implements AopProxy , InvocationHandler {

    // AdvisedSupper对象包含了代理所需的配置信息，如目标源、方法匹配器和方法拦截器。
    private final AdvisedSupper advisedSupper;

    /**
     * 构造函数，接收一个AdvisedSupper对象来初始化JdkDynamicAopProxy。
     * 
     * @param advisedSupper 包含代理配置信息的对象
     */
    public JdkDynamicAopProxy(AdvisedSupper advisedSupper) {
        this.advisedSupper = advisedSupper;
    }

    /**
     * 根据当前类加载器和目标源的接口生成一个代理对象。
     * 
     * @return 生成的代理对象
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupper.getTargetSource().getTargetClass(), this);
    }

    /**
     * 处理代理实例上的方法调用并返回结果。
     * 当与代理实例关联的方法被调用时，将调用此方法。
     * 
     * @param proxy 代理类实例
     * @param method 被调用的方法对象
     * @param args 方法参数
     * @return 方法调用的结果
     * @throws Throwable 方法调用中可能抛出的异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取到方法匹配器，通过AspectJExpressionPointcut解析传入的切点表达式，调用对应的matches方法判断是否为被代理类
        if (advisedSupper.getMethodMatcher().matches(method,advisedSupper.getTargetSource().getTarget().getClass())){

            // 获取到方法拦截器
            MethodInterceptor methodInterceptor = advisedSupper.getMethodInterceptor();

            // 创建ReflectiveMethodInvocation对象，封装了方法调用的相关信息
            ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(method, advisedSupper.getTargetSource().getTarget(), args);

            // 调用 MethodInterceptor 的 invoke 方法，传入 invocation 作为参数。
            // invoke 方法会执行拦截器逻辑，通常通过 invocation.proceed() 调用目标方法或下一个拦截器。
            return methodInterceptor.invoke(invocation);

        }

        // 如果方法匹配器判断当前调用的方法不满足切点表达式，则直接调用目标方法
        return method.invoke(advisedSupper.getTargetSource().getTarget(),args);
    }
}

```


### 2.2，测试案例

```java
public interface WorldService {  
    void sayHello();  
}

public class WorldServiceImpl implements WorldService{  
    @Override  
    public void sayHello() {  
        System.out.println("Hello World");  
    }  
}
```

```java
public class WorldServiceInterceptor implements MethodInterceptor {  
    @Override  
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {  
        System.out.println("before");  
        Object result = methodInvocation.proceed();  
        System.out.println("after");  
        return result;  
    }  
}
```

```java
public class DynamicProxyTest {  

    @Test  
    public void testDynamicProxy(){  
  
        AdvisedSupper advisedSupper = new AdvisedSupper();  
  
        WorldServiceImpl worldService = new WorldServiceImpl();  
        TargetSource targetSource = new TargetSource(worldService);  
        advisedSupper.setTargetSource(targetSource);  
  
        WorldServiceInterceptor interceptor = new WorldServiceInterceptor();  
        advisedSupper.setMethodInterceptor(interceptor);  
        // "execution(* org.springframework.test.service.WorldService.explode(..))"  
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.ioc.service.WorldService.sayHello(..))");  
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();  
        advisedSupper.setMethodMatcher(methodMatcher);  
  
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupper);  
        WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();  
        proxy.sayHello();  
  
  
    }  
}
```

## 3，JDK动态代理相关接口的内容补充


### 1.1，MethodInvocation


#### 作用

`MethodInvocation` 是一个接口，用于封装方法调用的上下文信息，主要用于 AOP 拦截器链的执行。它提供了以下功能：
- 保存目标方法、目标对象、参数等信息。
- 提供 `proceed()` 方法，用于继续执行拦截器链或目标方法。
- 支持获取方法调用的元信息（如方法名、参数等）。

#### 接口定义
```java
public interface MethodInvocation extends Invocation {
    Method getMethod();
    Object proceed() throws Throwable;
}
```
- `getMethod()`：获取当前调用的方法（`java.lang.reflect.Method` 对象）。
- `proceed()`：继续执行拦截器链的下一个拦截器，或者最终调用目标方法。
- 继承自 `Invocation`，可以获取参数（`getArguments()`）等信息。


#### 工作原理
- 在 Spring AOP 中，当一个方法被代理且匹配切点时，Spring 会构造一个 `MethodInvocation`（通常是 `ReflectiveMethodInvocation`）对象。
- `ReflectiveMethodInvocation` 内部维护一个拦截器链（`MethodInterceptor` 列表），通过 `proceed()` 方法按顺序执行每个拦截器，最终调用目标方法。

#### 示例
```java
ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(method, target, args);
// 在拦截器中调用
Object result = invocation.proceed(); // 执行下一个拦截器或目标方法
```

#### 上下文
- `MethodInvocation` 是 AOP 运行时的核心对象，主要用于**支持拦截器链的执行**。
- 它不直接处理代理逻辑，而是提供上下文给 `MethodInterceptor` 使用。

---

### 1.2，MethodInterceptor


#### 作用

`MethodInterceptor` 是一个接口，用于实现方法拦截器，定义在目标方法执行前后插入的切面逻辑（Advice）。它是 Spring AOP 中实现增强（如 `@Before`、`@After`、`@Around`）的核心。

#### 接口定义

```java
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
```
- `invoke` 方法接收一个 `MethodInvocation` 对象，执行拦截逻辑。
- 拦截器可以：
  - 调用 `invocation.proceed()` 继续执行下一个拦截器或目标方法。
  - 不调用 `proceed()`，直接返回自定义结果（绕过目标方法）。
  - 修改返回值、参数，或处理异常。

#### 工作原理

- Spring AOP 将切面逻辑（如日志、事务）封装为 `MethodInterceptor`。
- 多个 `MethodInterceptor` 组成一个拦截器链，按顺序执行。
- 每个拦截器通过 `MethodInvocation` 的 `proceed()` 方法决定是否继续执行。

#### 示例

一个日志拦截器：
```java
public class LoggingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: " + invocation.getMethod().getName());
        Object result = invocation.proceed(); // 执行下一个拦截器或目标方法
        System.out.println("After: " + invocation.getMethod().getName());
        return result;
    }
}
```


### 1.3，InvocationHandler


#### 作用
`InvocationHandler` 是 Java 动态代理（`java.lang.reflect.Proxy`）的核心接口，用于处理代理对象的逻辑。每次代理对象的方法被调用时，都会委托给 `InvocationHandler` 的 `invoke` 方法。

#### 接口定义

```java
public interface InvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```
- `proxy`：代理对象本身。
- `method`：被调用的方法（`java.lang.reflect.Method` 对象）。
- `args`：方法调用的参数。

#### 工作原理
- 使用 `Proxy.newProxyInstance` 创建动态代理时，需要提供一个 `InvocationHandler` 实现。
- 当代理对象的方法被调用时，JVM 会将调用转发到 `InvocationHandler` 的 `invoke` 方法。
- `invoke` 方法可以：
  - 直接调用目标方法（`method.invoke(target, args)`）。
  - 添加额外的逻辑（如日志、权限检查）。
  - 修改参数或返回值。

#### 示例
一个简单的动态代理：
```java
public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before: " + method.getName());
        Object result = method.invoke(target, args); // 调用目标方法
        System.out.println("After: " + method.getName());
        return result;
    }
}

// 使用
UserService target = new UserService();
InvocationHandler handler = new LoggingInvocationHandler(target);
UserService proxy = (UserService) Proxy.newProxyInstance(
    UserService.class.getClassLoader(),
    new Class[]{UserService.class},
    handler
);
proxy.getUser("123"); // 会打印 Before 和 After 日志
```



