在说Aware之前我们需要先明白Aware是做什么的，Aware有感知意识到的意思，简单来说就是我们如果想要让我们的Bean能够“感知”到我们的容器，这里不仅仅可以是容器还可能包含其他东西。
举个例子，我们定义一个Book的Bean，我们需要在这个Bean当中调用BeanFactory进行某些操作，那么该如何实现呢

```java
public class Book implements BeanFactoryAware {  

	private String name;
      
    private BeanFactory beanFactory;  
  
    public BeanFactory getBeanFactory() {  
        return beanFactory;  
    }  
  
    @Override  
    public void setBeanFactory(BeanFactory beanFactory) {  
        this.beanFactory = beanFactory;  
    }  
}
```

上面只是一个对应实现的演示，那么在获得到beanFactory之后可以做什么，也就不言而喻了。

下面我们来看一下具体的实现逻辑

首先我们需要定义Aware的夫接口，之后我们的所有不同的Aware具体功能接口都需要实现该接口

```java
/**  
 * Aware接口是Spring框架中一个标记接口，用于表示一个类可以被框架注入特定的依赖。  
 * 它并没有定义任何方法，而是作为一个标识，告知框架这个类需要进行特殊的初始化处理。  
 *  
 * @title Aware  
 * @date 2025/5/5 17:05  
 * @author jixu  
 */public interface Aware {  
}
```

之后以BeanFactoryAware为例

### 1，BeanFactoryAware

首先定义BeanFactoryAware接口实现Aware接口，定义setBeanFactory方法

```java
/**  
 * BeanFactoryAware接口是Spring Framework中的一部分，用于让实现该接口的bean在创建时能够获得BeanFactory的引用。  
 * 这个接口主要用于那些需要访问BeanFactory的组件，以便获取其他bean或者执行某些与bean管理相关的操作。  
 * 实现该接口的类需要提供setBeanFactory方法的具体实现，以便在bean初始化时接收BeanFactory的引用。  
 *  
 * @author jixu  
 * @title BeanFactoryAware  
 * @date 2025/5/5 17:06  
 */public interface BeanFactoryAware extends Aware{  
  
    public void setBeanFactory(BeanFactory beanFactory);  
}
```

之后我们需要在BeanFactory当中合适的地方去判断，指定的Bean是否继承了该接口，如果继承则通过该set方法注入到Bean当中即可

在AbstractAutowireCapableBeanFactory的initializeBean方法当中实现。

```java
/**  
 * 初始化Bean  
 * 执行Bean的初始化方法以及BeanPostProcessor的前置和后置处理方法  
 *  
 * @param beanName Bean名称  
 * @param bean Bean实例  
 */  
private void initializeBean(String beanName, Object bean,BeanDefinition beanDefinition) {  
    // 判断是否实现BeanFactoryAware接口，  
    if (bean instanceof BeanFactoryAware){  
        ((BeanFactoryAware)bean).setBeanFactory(this);  
    }  
  
    // 执行初始化之前的BeanPostProcessor前置处理  
    Object wrappedBean  = applyBeanPostProcessorsBeforeInitialization(bean, beanName);  
  
    // 执行初始化方法  
    try {  
        invokeInitMethods(beanName , wrappedBean , beanDefinition);  
    } catch (Exception e) {  
        throw new BeansException("调用 bean 的 init 方法[" + beanName + "] 失败", e);  
    }  
  
    // 执行初始化之前的BeanPostProcessor后置  
    wrappedBean = applyBeanPostProcessorsAfterInitialization(bean , beanName);  
}
```

好了到这里BeanFactoryAware就实现了，之后我们就可以在Bean当中获取到BeanFactory并进行操作。

再接下来，我们来实现一下ApplicationContextAware

### 2，ApplicationContextAware

```java
public interface ApplicationContextAware extends Aware {  
  
    public void setApplicationContext(ApplicationContext applicationContext);  
}
```

这里相对于BeanFactoryAware稍微复杂的地方在于，在ApplicationContext当中无法直接感知到具体是哪个Bean实现了该方法，需要调用set方法注入，这里就需要通过BeanPostProcess进行中间处理

```java
public interface ApplicationContextAware extends Aware {  
  
    public void setApplicationContext(ApplicationContext applicationContext);  
}
```

```java
public class ApplicationContextAwareProcessor implements BeanPostProcessor {  
  
  
    private final ApplicationContext applicationContext;  
  
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {  
        this.applicationContext = applicationContext;  
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
        // 如果该Bean实现ApplicationContextAware接口，注入ApplicationContext  
        if (bean instanceof ApplicationContextAware){  
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);  
        }  
        return bean;  
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
        return bean;  
    }  
}
```

```java
@Override  
public void refresh() {  
    // 通过子类创建BeanFactory，同时初始化beanDefinition  
    refreshBeanFactory();  
  
    // 获取到Bean工厂  
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();  
  
    // 对于ApplicationContextAware来说是作用在单个Bean当中的，其中ApplicationContext无法指定在哪个Bean当中生效  
    // 在这里可以通过BeanPostProcessor实现，此时的ApplicationContextAwarePostProcessor类似于一个中间件，将对象存储在当中  
    // 当PostProcessor接口识别到该类型的Bean则会将其注入进去  
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));  
  
    // 执行BeanFactoryPostProcess的方法  
    invokeBeanFactoryPostProcessors(beanFactory);  
  
    // 注册BeanPostPostProcess  
    registerBeanPostProcessors(beanFactory);  
  
    // 提前初始化单列Bean  
    beanFactory.preInstantiateSingletons();  
  
}
```