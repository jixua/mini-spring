
## 一，什么是Value注解的依赖注入

在 Spring 框架中，`@Value` 注解是一种常用的依赖注入方式，用于直接为 Bean 的属性注入值。以下是一个简单的示例：

```java
@Value("jixu")
private String name;
```

除了直接注入静态值，`@Value` 还支持属性占位符，能够从配置文件（如 `application.properties` 或 `application.yml`）中动态读取值。例如：

```java
@Value("${sex}")
private String sex;
```

通过这种方式，我们可以灵活地将外部配置的值注入到 Bean 的属性中。

## 二，实现原理

要理解 `@Value` 注解的工作原理，首先需要明确其作用的时机和位置。显然，`@Value` 的功能需要在 Bean 属性赋值操作之前完成。具体来说，我们需要修改 `BeanDefinition`，为其添加对应的 `PropertyValue`，从而确保在 Bean 实例化并执行属性赋值时，能够通过 `set` 方法正确注入值。

位置已经确定了，那么再具体一点需要依赖的组建也就是BeanPostProcess，再精确一点是InstantiationAwareBeanPostProcessor，也就是我们在实现AOP融入Bean生命周期的时候定义的接口，该接口是用于处理实例化的相关操作，因此该扩展功能也会在这里实现。我们通之前的操作逻辑一样会定义一个抽象方法用于属性赋值，之后在我们具体的实现类当中实现相关逻辑，再加入 到对应的BeanPostProcess容器当中，之后在AbstractAutowireCapableBeanFactory的对应位置进行处理即可。


1. 定义抽象方法：与 Spring 中其他扩展逻辑类似，我们可以先定义一个抽象方法，用于处理属性的赋值逻辑。
2. 实现具体逻辑：在具体的实现类中，解析 `@Value` 注解，提取注解中的值（或占位符），并将其转换为 `PropertyValue`，附加到 `BeanDefinition` 上。
3. 注册到容器：将实现的 `InstantiationAwareBeanPostProcessor` 加入到 Spring 的 `BeanPostProcessor` 容器中。
4. 集成到生命周期：Spring 会在 `AbstractAutowireCapableBeanFactory` 的适当位置调用我们的处理器，完成 `@Value` 注解的处理。


## 三，代码实现


### 1. 定义 @Value 注解

首先，定义自定义的 @Value 注解：

```java
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface Value {  
    String value();  
}
```

### 2. 实现 InstantiationAwareBeanPostProcessor

在 InstantiationAwareBeanPostProcessor 中添加用于处理 PropertyValues 的方法：

```java
PropertyValues postProcessPropertyValues(PropertyValues propertyValues , Object bean , String beanName);
```

### 3. 实现 AutowiredAnnotationBeanPostProcessor

定义 AutowiredAnnotationBeanPostProcessor 类，实现 InstantiationAwareBeanPostProcessor 接口，并在 postProcessPropertyValues 方法中实现具体的解析逻辑：


```java
  
@Component  
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor , BeanFactoryAware {  
  
  
    private ConfigurableBeanFactory beanFactory;  
  
  
    @Override  
    public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, Object bean, String beanName) {  
  
        Class<?> beanClass = bean.getClass();  
        PropertyValues pvs = new PropertyValues();  
  
        // 获取到当前类当中声明的所有属性  
        Field[] declaredFields = beanClass.getDeclaredFields();  
        for (Field field : declaredFields) {  
  
            // 获取到标记Value注解的属性  
            Value valueAnnotation = field.getAnnotation(Value.class);  
            if (valueAnnotation != null){  
                String value = valueAnnotation.value();  
  
                // 解析Value的属性值，判断是否需要替换占位符  
                value = beanFactory.resolveEmbeddedValue(value);  
                // 将解析完毕的字段添加到类属性当中  
                // BeanUtil.setFieldValue(bean,field.getName(),value);  
  
  
                pvs.addPropertyValue(new PropertyValue(field.getName(), value));  
  
            }  
        }  
  
        return pvs;  
    }  
  
  
  
  
  
    @Override  
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {  
        return null;  
    }  
  
    /**  
     * 在 Bean 初始化之前执行自定义处理逻辑。  
     * 使用此方法，可以在 Bean 被初始化之前对其进行修改或执行其他操作。  
     *  
     * @param bean     当前正在初始化的 Bean 实例。  
     * @param beanName 当前 Bean 的名称。  
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。  
     */  
    @Override  
    public Object postProcessBeforeInitialization(Object bean, String beanName) {  
        return null;  
    }  
  
    /**  
     * 在 Bean 初始化之后执行自定义处理逻辑。  
     * 使用此方法，可以在 Bean 初始化完成后对其进行进一步的修改或执行其他操作。  
     *  
     * @param bean     当前已经初始化的 Bean 实例。  
     * @param beanName 当前 Bean 的名称。  
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。  
     */  
    @Override  
    public Object postProcessAfterInitialization(Object bean, String beanName) {  
        return null;  
    }  
  
    @Override  
    public void setBeanFactory(BeanFactory beanFactory) {  
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;  
    }  
}
```

**说明**：postProcessPropertyValues 方法接收的 propertyValues 参数包含当前 Bean 的所有属性值，但在此处未充分利用（例如未检查属性重复）。方法通过反射获取字段，检查 @Value 注解，解析值后添加到一个新的 PropertyValues 对象中返回。相比官方实现（使用 MutablePropertyValues 合并原始 propertyValues），此实现进行了简化。

### 4. 占位符解析逻辑

在上述逻辑中，resolveEmbeddedValue 方法用于解析占位符。定义如下：

```java
// 在 ConfigurableBeanFactory 中定义
String resolveEmbeddedValue(String value);

/**
 * 添加属性解析器，以便解析嵌入值中的占位符
 * @param stringValueResolver 属性解析器
 */
void addEmbeddedValueResolver(StringValueResolver stringValueResolver);
```

由 AbstractBeanFactory 实现：

```java
/**  
 * 解析嵌入值，用于Value注解解析  
 *  
 * @param value * @return */@Override  
public String resolveEmbeddedValue(String value) {  
    String result = value;  
    for (StringValueResolver resolver : embeddedValueResolvers) {  
        // 会判断传入字段是否包含属性占位符，如果包含则替换为配置文件当中的值  
        result = resolver.resolveStringValue(result);  
    }  
    return result;  
  
}  
  
/**  
 * 添加属性解析器  
 *  
 * @param stringValueResolver */@Override  
public void addEmbeddedValueResolver(StringValueResolver stringValueResolver) {  
    embeddedValueResolvers.add(stringValueResolver);  
}
```

疑问解答：addEmbeddedValueResolver 的作用在于支持多个配置文件。Spring 可能配置多个 PropertyPlaceholderConfigurer，每个对应一个配置文件。通过添加多个 StringValueResolver，可以依次解析占位符，确保所有配置文件的属性值都被正确替换。

### 5. 定义 StringValueResolver 接口

定义工具类接口以抽象占位符解析逻辑：

```java
public interface StringValueResolver {  
  
    String resolveStringValue(String strVal);  
  
}
```


### 6. 实现 PlaceholderResolvingStringValueResolver

在 PropertyPlaceholderConfigurer 中定义内部类实现该接口：

定义一个内部类PlaceholderResolvingStringValueResolver实现该工具类的方法，这样就为所有的PropertyPlaceholderConfigurer提供了一层抽象层的实现，用于解析占位符。

```java
/**  
 * 定义字符解析器  
 */  
public class PlaceholderResolvingStringValueResolver implements StringValueResolver {  
  
    // 配置文件Properties对象  
    private final Properties properties;  
  
    public PlaceholderResolvingStringValueResolver(Properties properties) {  
        this.properties = properties;  
    }  
  
  
    @Override  
    public String resolveStringValue(String strVal) {  
        return PropertyPlaceholderConfigurer.this.resolverPlaceholder(strVal,properties);  
    }  
}
```

### 7. 注册解析器

在 PropertyPlaceholderConfigurer 的 postProcessBeanFactory 方法中注册解析器：


```java
public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {  
    // 加载属性配置文件  
    Properties properties = loadProperties();  
  
    // 属性值替换占位符  
    processProperties(beanFactory, properties);  
  
    // 添加属性解析器  
    StringValueResolver resolver = new PlaceholderResolvingStringValueResolver(properties);  
    beanFactory.addEmbeddedValueResolver(resolver);  
}
```

说明：postProcessBeanFactory 在容器刷新时执行，扫描所有 BeanFactoryPostProcessor 实现（如 PropertyPlaceholderConfigurer）。每个 PropertyPlaceholderConfigurer 对应一个配置文件，确保多配置文件场景下的占位符解析。

### 8. 集成到 BeanFactory

在 AbstractAutowireCapableBeanFactory 的 doCreateBean 方法中，实例化后、赋值前执行：

```java
// 通过InstantiationStrategy实例化Bean  
bean = createBeanInstance(beanDefinition);  
  
applyBeanPostprocessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);  
  
// 为Bean的属性进行赋值  
applyPropertyValues(bean , beanDefinition , beanName);
```

具体实现逻辑如下：

```java
private void applyBeanPostprocessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {  
    List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();  
    for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {  
        if (beanPostProcessor instanceof  InstantiationAwareBeanPostProcessor) {  
            PropertyValues propertyValues = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);  
            if (propertyValues != null) {  
                for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {  
                    beanDefinition.getPropertyValues().addPropertyValue(propertyValue);  
                }  
            }  
  
        }  
    }  
}
```


说明：该逻辑与 AOP 融入生命周期的实现类似，通过循环调用 BeanPostProcessor，处理并合并 PropertyValues。

