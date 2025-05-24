本章只介绍最简单的AOP基础功能的实现

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


### 2.2，总结


这里来重新梳理一下Jdk动态代理的流程，首先我们当前所实现的动态代理是基于最基础的继承而非注解形势，并且当前的AOP是一个最基础的底层产物，就类似于BeanFactory一样，我们还需要一个类似于ApplicationContext的类对当前的所有逻辑进行统筹才行。

那么我们先来看看到目前为止AOP的执行流程

1. 定义拦截器，实现MethodInterceptor，进行增强

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

2. 创建被增强类实例对象，拦截器实例对象

```java
WorldServiceImpl worldService = new WorldServiceImpl();  
TargetSource targetSource = new TargetSource(worldService); 
WorldServiceInterceptor interceptor = new WorldServiceInterceptor();
```

3. 定义切点表达式，通过AspectJExpressionPointcut进行解析，获取到方法匹配器

```java
AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* service.org.qlspringframework.beans.WorldService.sayHello(..))");  
MethodMatcher methodMatcher = pointcut.getMethodMatcher();  
```

4. 将增强类实例对象，拦截器实例对象以及方法匹配器实例对象注入到AdvisedSupper

```java
advisedSupper.setTargetSource(targetSource);  
advisedSupper.setMethodInterceptor(interceptor);  
advisedSupper.setMethodMatcher(methodMatcher);  
```


5. 创建Jdk代理对象传入AdvisedSupper对象，获取到代理类

```java
JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupper);  
WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();  
```

6. 强转代理类类型，调用对应方法

```java
WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();  
proxy.sayHello();  
```

7. 拦截代理类的方法调用，进入JdkDynamicAopProxy所实现的InvocationHandler的invoke方法
8. 在invoke当中进行判断切点是否正确
```java
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
```


9. 如果正确获取到adviceSupper当中保存到MethodInterceptor方法拦截器，调用增强逻辑

```java
@Override  
public Object invoke(MethodInvocation methodInvocation) throws Throwable {  
    System.out.println("before");  
    Object result = methodInvocation.proceed();  
    System.out.println("after");  
    return result;  
}
```

在调用invoke方法的过程当中会传入参数MethodInvocation methodInvocation，其中methodInvocation.proceed()方法通过 **责任链模式（Interceptor Chain）+ Java 反射** 实现，`proceed()` 方法会按顺序调用下一个拦截器，直到最终通过反射调用原始方法。

假设你有两个拦截器

[LogInterceptor, TransactionInterceptor]

调用栈如下：

```scss
LogInterceptor.invoke() {
    -> proceed()
        -> TransactionInterceptor.invoke() {
            -> proceed()
                -> method.invoke(target, args) // 原始方法
        }
}

```

这是理论上对于AOP的设计，但是当前只支持单个拦截器的拦截调用，后续会逐步添加。


### 2.3，测试案例

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
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* service.org.qlspringframework.beans.WorldService.sayHello(..))");  
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();  
        advisedSupper.setMethodMatcher(methodMatcher);  
  
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupper);  
        WorldService proxy = (WorldService) jdkDynamicAopProxy.getProxy();  
        proxy.sayHello();  
  
  
    }  
}
```

# 二，Cglib动态代理

Cglib动态代理和Jdk动态代理的实现并无太多的不同，二者之家的区别只在于底层实现方式。

直接看实现代码

#CglibDynamicAopProxy

```java
public class CglibDynamicAopProxy implements AopProxy {  
  
    // AdvisedSupper 对象中包含了目标类的信息以及切面的配置  
    private final AdvisedSupper advisedSupper;  
  
    // 构造方法，初始化 CglibDynamicAopProxy 对象  
    public CglibDynamicAopProxy(AdvisedSupper advisedSupper) {  
        this.advisedSupper = advisedSupper;  
    }  
  
    /**  
     * 获取代理对象  
     *  
     * @return 代理对象，通过该对象可以调用目标方法以及切面方法  
     */  
    @Override  
    public Object getProxy() {  
        // 创建 CGLIB 提供的增强器对象（核心代理创建类）  
        Enhancer enhancer = new Enhancer();  
  
        // 设置被代理类的父类（目标对象的真实类）  
        enhancer.setSuperclass(advisedSupper.getTargetSource().getTarget().getClass());  
  
        // 设置代理对象实现的接口（可选）  
        enhancer.setInterfaces(advisedSupper.getTargetSource().getTargetInterfaceClass());  
  
        // 设置方法拦截器，用于处理方法调用  
        enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupper));  
  
        // 创建并返回代理对象  
        return enhancer.create();  
    }  
  
  
  
    // CglibMethodInvocation 类用于处理 CGLIB 方法调用  
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation{  
        private final MethodProxy methodProxy;  
  
        /**  
         * 构造方法，初始化ReflectiveMethodInvocation对象  
         *  
         * @param method   方法对象，表示要调用的方法  
         * @param target   目标对象，即要调用方法的实例  
         * @param argument 方法参数数组，存储调用方法时传递的参数  
         */  
        public CglibMethodInvocation(Method method, Object target, Object[] argument, MethodProxy methodProxy) {  
            super(method, target, argument);  
            this.methodProxy = methodProxy;  
        }  
  
        /**  
         * 执行当前方法调用  
         *  
         * @return 方法执行结果  
         * @throws Throwable 方法执行过程中抛出的异常  
         */  
        @Override  
        public Object proceed() throws Throwable {  
            // 使用 MethodProxy 执行方法，提高执行效率  
            return this.methodProxy.invoke(target,argument);  
        }  
    }  
  
    // DynamicAdvisedInterceptor 类用于适配 CGLIB 的 MethodInterceptor 接口  
    private static class DynamicAdvisedInterceptor implements MethodInterceptor {  
        private final AdvisedSupper advisedSupper;  
  
        private DynamicAdvisedInterceptor(AdvisedSupper advisedSupper) {  
            this.advisedSupper = advisedSupper;  
        }  
  
        /**  
         * 拦截方法调用  
         *  
         * @param o        代理对象  
         * @param method   被调用的方法对象  
         * @param objects  方法参数数组  
         * @param methodProxy  方法代理对象，用于调用目标方法  
         * @return 方法执行结果  
         * @throws Throwable 方法执行过程中抛出的异常  
         */  
        @Override  
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {  
            // 创建 CglibMethodInvocation 对象，封装方法调用信息  
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(method, advisedSupper.getTargetSource().getTarget(), objects, methodProxy);  
  
            // 如果切点表达式匹配  
            if (advisedSupper.getMethodMatcher().matches(method,advisedSupper.getTargetSource().getTarget().getClass())){  
                // 使用切面的 MethodInterceptor 处理方法调用  
                return advisedSupper.getMethodInterceptor().invoke(methodInvocation);  
            }  
            // 如果切点表达式不匹配，则直接调用原始方法  
            return method.invoke(o,objects);  
        }  
    }  
}
```







# 三、AOP 核心术语体系（来自经典 AOP 理论）

| 概念             | 作用                         | 类名/注解               |
| -------------- | -------------------------- | ------------------- |
| **Join Point** | 可插入切面的程序点                  | 方法、构造器、字段等          |
| **Pointcut**   | 匹配 Join Point 的表达式         | `execution(...)` 等  |
| **Advice**     | 实际执行的“通知”逻辑                | `Before`, `After` 等 |
| **Aspect**     | 切面（由 Advice + Pointcut 组成） | `@Aspect` 类         |
| **Weaving**    | 将 Advice 编织到目标类            | 编译期 / 运行期（如代理）      |
| **Target**     | 被代理的业务对象                   |                     |

所以，“Advice” 不是 Spring 起的，而是 **AOP 理论本身的专业术语**，用来描述：

> **与主业务逻辑相分离的、可复用的横切关注逻辑模块**

## Advice

`Advice` 这个单词的本意是：

> 建议、忠告、指引

在 AOP（面向切面编程）中，它的含义可以理解为：

> 在你执行主业务逻辑之前、之后或异常时，我给你一个‘建议’性的操作，比如加个日志、记录个事务、处理权限等。

所以 Spring、AOP Alliance 采用了这个术语 —— `Advice` 来指代所有横切关注点逻辑的实现。是 AOP 中对“横切关注点代码”的专业称呼，用来表示“你在主业务流程中可以附加的一段建议性逻辑”。