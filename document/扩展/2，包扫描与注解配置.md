

#  注解配置与包扫描的实现机制

## 一、概述：什么是注解配置与包扫描？

在基于注解的 Spring 样式容器中，包扫描（Package Scanning）与注解配置（Annotation Configuration） 是核心的自动化注册机制：

> 本质上，它通过扫描指定包路径下的类，识别其中包含特定注解（如 `@Component`, `@Scope`），并将其自动注册为容器中的 Bean。

---

## 二、处理流程概览

要实现注解注册 Bean 的机制，大致流程如下：

1. **确定扫描路径**：通常由配置文件（如 XML）提供；

2. **扫描类文件**：获取指定包路径下所有类；

3. **筛选目标类**：识别包含目标注解的类，如 `@Component`；

4. **构建 BeanDefinition**：为每个匹配类生成对应的 BeanDefinition；

5. **注册 BeanDefinition**：将生成的 BeanDefinition 注册到 `BeanDefinitionMap` 中。


此流程应发生在 **BeanDefinition 的加载阶段**，因此其集成逻辑最终应写入 `XmlBeanDefinitionReader` 中。我们可以将功能模块解耦为：

- **扫描器模块**：负责扫描、识别和构建 BeanDefinition；

- **注册器集成**：负责注册这些 BeanDefinition。


---

## 三、注解定义

### @Component

用于标记一个类为容器可管理的组件：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
```

### @Scope

用于定义组件的作用域（如 `singleton` / `prototype`）：

```java
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value() default "singleton";
}
```

---

## 四、核心代码结构

### 1. `ClassPathScanningCandidateComponentProvider`

用于扫描指定包路径下所有带 `@Component` 注解的类，并构建对应的 BeanDefinition：

```java
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();

        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }

        return candidates;
    }
}
```

---

###  2. `ClassPathBeanDefinitionScanner`

继承扫描器，实现更完整的处理逻辑：

- 解析作用域（`@Scope`）；
    
- 解析 Bean 名称（默认类名首字母小写）；
    
- 完成 BeanDefinition 的注册。
    

```java
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegister register;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegister register) {
        this.register = register;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = super.findCandidateComponents(basePackage);

            for (BeanDefinition candidate : candidates) {
                // 设置作用域
                String scope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(scope)) {
                    candidate.setScope(scope);
                }

                // 设置 Bean 名称并注册
                String beanName = determineBeanName(candidate);
                register.registerBeanDefinition(beanName, candidate);
            }
        }
    }

    private String determineBeanName(BeanDefinition definition) {
        Class<?> clazz = definition.getBeanClass();
        Component component = clazz.getAnnotation(Component.class);
        String value = component.value();
        return StrUtil.isEmpty(value) ? StrUtil.lowerFirst(clazz.getSimpleName()) : value;
    }

    private String resolveBeanScope(BeanDefinition definition) {
        Scope scope = definition.getBeanClass().getAnnotation(Scope.class);
        return scope != null ? scope.value() : StrUtil.EMPTY;
    }
}
```
