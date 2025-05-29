
## 全面理解 AOP：从核心概念到 JDK/CGLIB 动态代理实现

在构建可维护、高复用的业务系统时，**面向切面编程（AOP）**是不可或缺的技术之一。它通过将日志、安全、事务等“横切关注点”从主业务逻辑中剥离，大大提升了系统的可扩展性与清晰性。

本文将从 AOP 的核心概念出发，逐步深入到 JDK 与 CGLIB 动态代理的底层实现，帮助你构建对 AOP 更系统、扎实的理解。

---

## 一、AOP 核心概念详解

### 1. Advice（增强）

**定义**：Advice 是指实际织入目标方法的增强逻辑，如日志记录、权限校验、事务处理等。

常见类型包括：

- `BeforeAdvice`：在目标方法 **执行前** 执行。

- `AfterReturningAdvice`：在目标方法 **正常返回后** 执行。

- `AfterThrowingAdvice`：在目标方法 **抛出异常时** 执行。

- `AroundAdvice`：环绕通知，可在方法调用前后执行自定义逻辑。


📌 **作用**：将通用功能模块与核心业务解耦，提升代码复用与可维护性。

---

### 2. Pointcut（切点）

**定义**：Pointcut 是用于筛选连接点的规则表达式，用于指定哪些类/方法需要增强。

如 AspectJ 表达式：

```java
execution(public * com.example.service..*Service.*(..))
```

📌 **作用**：通过表达式精确匹配目标方法，控制 Advice 应用范围。

---

### 3. JoinPoint（连接点）

**定义**：程序执行过程中的某个点。在 Spring AOP 中，连接点主要指 **方法执行的瞬间**。

📌 **作用**：是 Advice 能插入的实际执行点。

---

### 4. Advisor（通知器）

**定义**：Advisor 是 Advice 与 Pointcut 的组合体，表示“在哪些连接点使用什么增强逻辑”。

通常组成：

- `AspectJExpressionPointcut`（切点）
    
- `MethodInterceptor`（增强逻辑）
    

📌 **作用**：构建拦截器链的核心单位，是自动代理机制识别目标类的依据。

---

### 5. Proxy（代理对象）

**定义**：代理对象是目标对象的包装器，通过它调用方法时实现增强逻辑的织入。

Spring 提供两种方式：

- **JDK 动态代理**：基于接口

- **CGLIB 动态代理**：基于继承


**作用**：是 AOP 编程的具体载体。

---

### ☕ 总结关系图：

```
         +-------------+
         |   Advisor   | => [ Pointcut + Advice ]
         +-------------+
                ↓
         +-------------+
         |   Proxy     | --（生成）--> 包装目标对象
         +-------------+
                ↓
         +-------------+
         |  JoinPoint  | => 方法执行点
         +-------------+
                ↑
         +-------------+
         |   Advice    | => 织入增强逻辑
         +-------------+
```

---

## 二、JDK 动态代理详解（基于接口）

### 使用场景

- 目标类实现了接口

- 你希望在 **不修改源码** 的情况下添加增强逻辑


---

### 实现步骤

#### 1. 定义接口与实现类

```java
public interface HelloService {
    void sayHello();
}

public class HelloServiceImpl implements HelloService {
    public void sayHello() {
        System.out.println("Hello, world!");
    }
}
```

#### 2. 实现 InvocationHandler

```java
public class MyInvocationHandler implements InvocationHandler {
    private final Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[JDK] Before: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("[JDK] After: " + method.getName());
        return result;
    }
}
```

#### 3. 创建代理对象

```java
HelloService proxy = (HelloService) Proxy.newProxyInstance(
    target.getClass().getClassLoader(),
    target.getClass().getInterfaces(),
    new MyInvocationHandler(target)
);
```

---

### ⚠️ 注意事项

- 只能代理接口，不能代理普通类。

- 被代理方法不能是 `final`。

- 返回对象必须强转为接口类型。


---

## 三、CGLIB 动态代理详解（基于继承）

### 使用场景

- 目标类 **未实现接口**
    
- 或希望对类本身（而非接口）进行增强
    

---

### 实现步骤

#### 1. 引入 CGLIB 依赖

```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.3.0</version>
</dependency>
```

#### 2. 定义目标类

```java
public class HelloService {
    public void sayHello() {
        System.out.println("Hello, world!");
    }
}
```

#### 3. 实现 MethodInterceptor

```java
public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("[CGLIB] Before: " + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("[CGLIB] After: " + method.getName());
        return result;
    }
}
```

#### 4. 创建代理对象

```java
Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(HelloService.class);
enhancer.setCallback(new MyMethodInterceptor());

HelloService proxy = (HelloService) enhancer.create();
```

---

### 注意事项

- CGLIB 基于继承实现，因此：

    - 不能代理 `final` 类

    - 方法不能为 `final` 或 `private`

- Spring 在未实现接口时默认采用 CGLIB 创建代理



