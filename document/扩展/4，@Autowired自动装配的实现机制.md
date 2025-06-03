

# `@Autowired` 注解实现原理详解

`@Autowired` 的注入机制与 `@Value` 注解非常相似，不同之处在于：

- `@Value` 主要注入的是常量值或配置项（如 `${}` 表达式）；
    
- 而 `@Autowired` 注入的是 Spring 容器中的 **Bean 实例**，也即对象引用。
    

---

## 一、`@Autowired` 注解定义

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
```

> 默认情况下，`@Autowired` 按类型注入（byType）。如果容器中存在多个相同类型的 Bean，会默认注入第一个；如需精确指定，可以配合 `@Qualifier` 使用。

---

## 二、@Qualifier 注解辅助指定 Bean 名称

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.TYPE, ElementType.ANNOTATION_TYPE
})
@Inherited
@Documented
public @interface Qualifier {
    String value() default "";
}
```

`@Qualifier` 允许通过名称精确指定注入的 Bean，从而避免类型冲突或歧义。

---

## 三、BeanFactory：按类型获取 Bean

为了支持类型注入，我们需要在 `BeanFactory` 接口中添加一个按类型获取 Bean 的方法：

```java
/**
 * 根据指定类型获取 Bean 实例。
 * 若存在多个该类型的 Bean，可能抛出异常。
 */
<T> T getBean(Class<T> requiredType);
```

在 `DefaultListableBeanFactory` 中进行实现：

```java
@Override
public <T> T getBean(Class<T> requiredType) throws BeansException {
    List<String> beanNames = new ArrayList<>();
    for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
        Class<?> beanClass = entry.getValue().getBeanClass();
        if (requiredType.isAssignableFrom(beanClass)) {
            beanNames.add(entry.getKey());
        }
    }

    if (beanNames.size() == 1) {
        return super.getBean(beanNames.get(0), requiredType);
    }

    throw new BeansException(requiredType + 
        " expected single bean but found " + 
        beanNames.size() + ": " + beanNames);
}
```

---

## 四、注入逻辑实现

`@Autowired` 的实现依赖于 Spring 的扩展点 `BeanPostProcessor`，更具体地说，是在 `InstantiationAwareBeanPostProcessor` 接口中重写的 `postProcessPropertyValues` 方法完成注入逻辑。

以下是字段注入的核心实现逻辑（简化版）：

```java
for (Field field : declaredFields) {
    // 检查是否标注 @Autowired
    Autowired autowired = field.getAnnotation(Autowired.class);
    if (autowired != null) {
        Class<?> type = field.getType();
        Object dependentBean;

        // 优先检查 @Qualifier 注解
        Qualifier qualifier = field.getAnnotation(Qualifier.class);
        if (qualifier != null) {
            String beanName = qualifier.value();
            dependentBean = beanFactory.getBean(beanName);
        } else {
            // 根据类型获取 Bean
            dependentBean = beanFactory.getBean(type);
        }

        // 直接通过反射注入字段（此处未处理复杂依赖）
        BeanUtil.setFieldValue(bean, field.getName(), dependentBean);

        // 原生 Spring 会构建 PropertyValue 并走完整依赖注入流程
        // pvs.addPropertyValue(new PropertyValue(field.getName(), dependentBean));
    }
}
```

>  本实现简化了原生 Spring 中的依赖注入逻辑，未处理循环依赖、构造器注入、多候选 Bean 等复杂情况。



## 五、小结

- `@Autowired` 实现基于反射和后置处理器（`BeanPostProcessor`）；

- 默认按类型注入，配合 `@Qualifier` 可精确按名称注入；

- 实际注入发生在 Bean 实例化之后，属性填充阶段；

- 与 `@Value` 类似，都通过 `InstantiationAwareBeanPostProcessor` 的 `postProcessPropertyValues` 方法实现。
