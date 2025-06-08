

在 AOP（面向切面编程）中，Spring 支持两种常见的代理机制：基于接口的 JDK 动态代理 和 基于字节码生成的 CGLIB 动态代理。而 **代理工厂（`ProxyFactory`）的引入，正是为了解决硬编码选择代理方式的问题**，通过统一的封装，自动选择最合适的代理实现方式。

为了支持自动判断使用哪种代理方式，我们需要在代理配置信息中引入一个开关字段：

###  `AdvisedSupport`（代理配置信息类）

```java
// 是否强制使用 CGLIB 代理（即代理目标类本身，而不是接口）
private boolean proxyTargetClass = false;
```

这个字段是判断使用 JDK 还是 CGLIB 的关键标识。


###  `ProxyFactory`（代理工厂类）

```java
/**
 * 代理工厂类
 * 
 * 用于根据配置生成对应的 AOP 代理对象。
 * 支持两种代理方式：
 *   - JDK 动态代理（适用于目标类实现了接口）
 *   - CGLIB 动态代理（适用于目标类未实现接口，或需要代理类本身）
 * 
 * @author jixu
 * @date 2025/5/24
 */
public class ProxyFactory {

    // 封装了代理配置（切点、拦截器、目标对象等）
    private final AdvisedSupper advisedSupport;

    /**
     * 构造方法
     * 
     * @param advisedSupport 配置信息容器
     */
    public ProxyFactory(AdvisedSupper advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    /**
     * 获取代理对象
     * 
     * @return 生成的代理对象（JDK 或 CGLIB）
     */
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    /**
     * 创建 AOP 代理对象
     * 
     * 根据配置决定使用 JDK 动态代理还是 CGLIB 代理。
     * 
     * @return AopProxy 实现类
     */
    public AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            // 需要代理类本身，使用 CGLIB
            return new CglibDynamicAopProxy(advisedSupport);
        }
        // 默认使用 JDK 动态代理
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
```

---

### 小结

- `ProxyFactory` 封装了代理选择逻辑，避免手动 `new` 代理方式。

- `AdvisedSupper` 是代理配置的核心容器。

- `proxyTargetClass = true` ⇒ 使用 CGLIB（代理目标类本身）

- 否则 ⇒ 使用 JDK 动态代理（基于接口）


### 测试

```java
@Test  
public void testProxyFactory(){  
    advisedSupport.setProxyTargetClass(true);  
    ProxyFactory proxyFactory = new ProxyFactory(advisedSupport);  
    WorldService worldService = (WorldService) proxyFactory.getProxy();  
    worldService.sayHello();  
  
}
```