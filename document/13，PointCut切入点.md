
Spring的PointCut切入点以及相关切点表达式的解析逻辑实际上不是在Spring当中自己实现的，而是调用了AspectJ的相关接口，这也是为什么在Spring AOP当中会大量出现AspectJ相关注解如，@Aspect，@Before等等

## 一，AspectJ与Spring AOP之间的关系

Spring AOP是利用的动态代理机制，如果一个Bean实现了接口，那么就会采用JDK动态代理来生成该接口的代理对象，如果一个Bean没有实现接口，那么就会采用CGLIB来生成当前类的一个代理对象。代理对象的作用就是代理原本的Bean对象，代理对象在执行某个方法时，会在该方法的基础上增加一些切面逻辑，使得我们可以利用AOP来实现一些诸如登录校验、权限控制、日志记录等统一功能。

Spring AOP和Aspect之间并没有特别强的关系，AOP表示面向切面编程，这是一种思想，各个组织和个人都可以通过技术来实现这种思想，AspectJ就是其中之一，它会在编译期来对类进行增强，所以要用Aspect，得用Aspect开发的编译器来编泽你的项目。而Spring AOP则是米用动态代理的方式来实现AOP，只不过觉得Aspect中设计的那几个注解比较好，比如@Before、@After、@Around等，同时也不给程序员造成困扰，所以Spring AOP中会对这几个注解进行支持，虽然注解是相同的，但是底层的支持实现是完全不一样的。


## 二，底层实现

#PointCut

定义切点接口，用于选择性地应用通知（Advice）到目标类的特定方法上
切点通过类过滤器（ClassFilter）和方法匹配器（MethodMatcher）来确定应用通知的方法  

```java
/**  
 * 定义切点接口，用于选择性地应用通知（Advice）到目标类的特定方法上  
 * 切点通过类过滤器（ClassFilter）和方法匹配器（MethodMatcher）来确定应用通知的方法  
 *  
 * @author jixu  
 * @title PointCut  
 * @date 2025/5/20 15:47  
 */
public interface PointCut {  
  
    /**  
     * 获取类过滤器，用于判断哪些类需要应用通知  
     *  
     * @return ClassFilter 实例，用于过滤目标类  
     */  
    ClassFilter getClassFilter();  
  
    /**  
     * 获取方法匹配器，用于判断目标类中的哪些方法需要应用通知  
     *  
     * @return MethodMatcher 实例，用于匹配目标方法  
     */  
    MethodMatcher getMethodMatcher();  
}
```

#MethodMatcher

MethodMatcher接口用于定义方法匹配规则。它主要用于判断一个给定的方法是否与当前上下文中的方法匹配。
这个接口在需要对方法进行筛选或条件判断时非常有用， 例如在AOP（面向切面编程）中，确定哪些方法需要应用切面逻辑。   

```java
/**  
 * MethodMatcher接口用于定义方法匹配规则。  
 * 它主要用于判断一个给定的方法是否与当前上下文中的方法匹配。  
 * 这个接口在需要对方法进行筛选或条件判断时非常有用，  
 * 例如在AOP（面向切面编程）中，确定哪些方法需要应用切面逻辑。  
 *  
 * @author jixu  
 * @title MethodMatcher  
 * @date 2025/5/20 15:46  
 */
public interface MethodMatcher {  
  
    /**  
     * 判断给定的方法是否与当前上下文中的方法匹配。  
     *  
     * @param method 要检查的方法  
     * @param targetClass 目标类，即方法所属的类  
     * @return 如果方法匹配则返回true，否则返回false  
     */    
     boolean matches(Method method , Class<?> targetClass);  
}
```

#ClassFilter

ClassFilter接口用于定义类过滤器，它帮助筛选出满足特定条件的类  

```java
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
```

#AspectJExpressionPointcut

该类主要用于定义AspectJ的切点表达式，以便在面向切面编程（AOP）中 精确定义哪些方法或代码块需要应用切面逻辑，如日志记录、性能监控等  

```java
  
/**  
 * AspectJ表达式切点类  
 * 该类主要用于定义AspectJ的切点表达式，以便在面向切面编程（AOP）中  
 * 精确定义哪些方法或代码块需要应用切面逻辑，如日志记录、性能监控等  
 *   
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
     */    @Override  
    public boolean matches(Class<?> clazz) {  
        return pointcutExpression.couldMatchJoinPointsInType(clazz);  
    }  
  
    /**  
     * 判断给定的方法是否与当前上下文中的方法匹配。  
     *  
     * @param method      要检查的方法  
     * @param targetClass 目标类，即方法所属的类  
     * @return 如果方法匹配则返回true，否则返回false  
     */    @Override  
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
```


## 三，测试


```java
public class PointCutTest {  
  
    @Test  
    public void testPointCut() throws NoSuchMethodException {  
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.qlspringframework.beans.ioc.service.HelloService.*(..))");  
  
        Class<HelloService> helloServiceClass = HelloService.class;  
        Method declaredMethod = helloServiceClass.getDeclaredMethod("say");  
  
        Assert.assertEquals(pointcut.matches(helloServiceClass),true);  
        Assert.assertEquals(pointcut.matches(declaredMethod,helloServiceClass),true);  
  
    }  
}
```