
要在 Spring 的 Bean 生命周期中融入类型转换功能，首先需要明确类型转换的适用场景。Spring 主要在以下两个环节中需要进行类型转换：

1. **处理 @Value 注解的属性赋值**：将配置文件或注解中的值（如字符串）转换为目标字段的类型。
2. **为 Bean 填充属性**：在依赖注入时，确保注入的值与目标字段的类型匹配。

上一节已详细介绍了 Spring 如何实现属性占位符替换逻辑。本节将重点说明如何将类型转换功能集成到 Spring 的 IoC 容器中。


## 一，类型转换的处理时机

### 对于Value注解的处理


```java
// 获取到当前类当中声明的所有属性  
Field[] declaredFields = beanClass.getDeclaredFields();  
for (Field field : declaredFields) {  
  
    // 获取到标记Value注解的属性  
    Value valueAnnotation = field.getAnnotation(Value.class);  
    if (valueAnnotation != null){  
        Object value = valueAnnotation.value();  
  
        // 解析Value的属性值，判断是否需要替换占位符  
        value = beanFactory.resolveEmbeddedValue((String) value);  
  
        Class<?> sourceClass = value.getClass();  
        Class<?> targetType = (Class<?>) TypeUtil.getType(field);  
  
        // 类型转换  
        ConversionService conversionService = beanFactory.getConversionService();  
        if (conversionService != null){  
            boolean canConvert = conversionService.canConvert(sourceClass, targetType);  
            if (canConvert){  
                value = conversionService.convert(value,targetType);  
            }  
  
        }  
  
        // 将解析完毕的字段添加到类属性当中  
        // BeanUtil.setFieldValue(bean,field.getName(),value);  
        pvs.addPropertyValue(new PropertyValue(field.getName(), value));  
  
    }  
}
```

在解析Value的属性值，判断是否需要替换占位符之后，我们需要判断当前该字段的类型（加入要注入的字段是Integer age，那么我们从配置文件读取后返回的必然是String类型，那么这里就需要尝试将String转为Integer），尝试执行转型。

具体的转换逻辑实际上也非常简单，先从容器当中获取到类型转换器，判断当前类型是否支持转换，如果支持则进行转换。


### 对于Bean的属性填充


```java
// 如果是Bean引用，则获取对应的Bean实例  
if (value instanceof BeanReference){  
    BeanReference beanReference = (BeanReference) value;  
    value = super.getBean(beanReference.getBeanName());  
}else {  
    Class<?> sourceClass = value.getClass();  
    Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);  
  
    ConversionService conversionService = getConversionService();  
    if (conversionService != null) {  
        boolean canConvert = conversionService.canConvert(sourceClass, targetType);  
        if (canConvert) {  
            value = conversionService.convert(value, targetType);  
        }  
    }  
}
```

这里其实逻辑和上面的类似就不过多赘述了。



## 二，将类型转换器加入到容器当中

想要将类型转换器加入到容器当中需要在ApplicationContext的阶段进行处理

在这里需要判断BeanDefinition当中是否存在名为conversionService的类型转换器对象，如果存在则尝试获取并将该对象注入到BeanFactory当中

`AbstractApplicationContext`


```java
public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

//注册类型转换器和提前实例化单例bean  
finishBeanFactoryInitialization(beanFactory);
```

```java
private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {  
    if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME)) {  
        Object bean = beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME);  
        if ((bean instanceof ConversionService)){  
            beanFactory.setConversionService((ConversionService) bean);  
        }  
    }  
    // 提前初始化单列Bean  
    beanFactory.preInstantiateSingletons();  
}
```

逻辑说明：
- Bean 检查：检查容器中是否存在名为 conversionService 的 Bean。
- 类型验证：确认该 Bean 是 ConversionService 类型。
- 注册：将 ConversionService 注入到 BeanFactory，供后续属性注入和 @Value 解析使用。

这里我们新添加一个方法用于处理添加类型转换器conversionService与实例化Bean

这里我们尝试加入到Spring当中的默认转化器如下，会通过构造方法将当前已有的转换器注入到容器当中，以便于后续的调用

```java
@Component("conversionService")  
public class DefaultConversionService extends GenericConversionService{  
  
    public DefaultConversionService() {  
        addDefaultConverters(this);  
  
    }  
  
    private void addDefaultConverters(ConverterRegister converterRegister) {  
        converterRegister.addConverterFactory(new StringToNumberConverterFactory());  
    }  
  
  
}
```

逻辑说明：
- 默认转换器：DefaultConversionService 通过构造方法注册内置转换器，如 StringToNumberConverterFactory，支持字符串到数字（如 Integer、Double）的转换。
- 扩展性：开发者可以通过 ConverterRegistry 添加自定义转换器，满足特定需求。