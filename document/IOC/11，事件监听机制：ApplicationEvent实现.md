
## 一，Spring容器事件与监听器的作用

Spring框架中的容器事件和事件监听机制提供了一种观察者模式的实现，主要用于在Spring容器生命周期中的关键点或应用状态变化时进行通知和处理。以下是其主要作用：

### 容器事件的作用

1. **生命周期通知**：在ApplicationContext初始化、刷新、关闭等关键阶段发出事件
2. **状态变更通知**：当bean被加载、刷新或销毁时发出通知
3. **应用事件传播**：允许应用自定义事件并在容器内传播

### 容器事件监听器的作用

1. **响应式处理**：监听特定事件并执行相应业务逻辑
2. **解耦组件**：实现发布-订阅模式，减少组件间的直接依赖
3. **扩展点**：通过监听容器事件来扩展框架功能
4. **有序处理**：可以通过@Order注解控制多个监听器的执行顺序

### 主要内置事件类型

- `ContextRefreshedEvent`：容器初始化或刷新完成时发布
- `ContextStartedEvent`：调用start()方法后发布
- `ContextStoppedEvent`：调用stop()方法后发布
- `ContextClosedEvent`：容器关闭时发布
- `RequestHandledEvent`：HTTP请求处理完成后发布

### 使用示例

```java
// 定义监听器
@Component
public class MyContextListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 容器刷新完成后执行的逻辑
    }
}

// 或使用注解方式
@Component
public class MyAnnotationListener {
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        // 处理事件
    }
}
```

这种机制使得应用程序能够以松耦合的方式响应容器状态变化，是实现初始化逻辑、资源加载和清理工作的理想选择。


## 二，实现原理

从上面的代码我们也可以看得出来，在Spring的事件监听当中，包含了几个主要部分，对容器刷新对监听、对容器关闭的监听以及自定义监听。很显然，前两者与自定义监听并没有多少实质性的差别，只是在此基础上我们在容器的对应位置手动实现罢了。
那么我们现在来看一下代码的实现逻辑

首先容器监听，实际上是基于观察者模式实现的，简单粗暴的来讲，就是当一个事件发生了变化，可以通知他的监听者做出对应的操作。
那么我们就需要实现监听者，发布者，监听事件这三个核心概念。

### 1，定义事件

#ApplicationEvent

```java
/**  
 * ApplicationEvent 类是所有应用事件的基类。  
 * 它继承自 EventObject 类，并在构造函数中要求提供事件的源对象。  
 * 这个类是抽象的，意味着它不能直接被实例化，只能被子类继承。  
 *  
 * @author jixu  
 * @title ApplicationEvent  
 * @date 2025/5/19 17:14  
 */
 public abstract class ApplicationEvent extends EventObject {  
    /**  
     * 构造一个原型事件。  
     *  
     * @param source 事件最初发生的对象。  
     * @throws IllegalArgumentException 如果 source 为 null，则抛出此异常。  
     */  
    public ApplicationEvent(Object source) {  
        super(source);  
    }  
}
```

#ApplicationContextEvent

`ApplicationContextEvent`是对`ApplicationEvent`的进一步抽取，主要用于处理应用上下文事件

```java
  
/**  
 * 该类是ApplicationContext事件的抽象基类，继承自ApplicationEvent  
 * 它为所有的应用上下文事件提供了一个通用的父类，使得这些事件可以共享一些通用的行为和属性  
 *   
* @author jixu  
 * @title ApplicationContextEvent  
 * @date 2025/5/19 17:18  
 */
 public abstract class ApplicationContextEvent extends ApplicationEvent {  
  
    /**  
     * 构造一个原型事件对象  
     *  
     * @param source 事件最初发生的对象  
     * @throws IllegalArgumentException 如果source为null，则抛出该异常  
     */  
    public ApplicationContextEvent(Object source) {  
        super(source);  
    }  
  
    /**  
     * 获取事件源对象，即应用上下文事件的来源  
     * 这个方法允许事件处理者获取到触发事件的上下文对象  
     *   
	 * @return 返回事件源对象，即应用上下文事件的来源  
     */  
    public ApplicationContextEvent getApplicationContext() {  
        return (ApplicationContextEvent) getSource();  
    }  
}
```

### 2，事件监听

#ApplicationListener

ApplicationListener是所有事件监听者的父类，可通过泛型指定具体的监听对象，通过重写onApplicationEvent方法，会在监听到事件后调用该方法

```java
/**  
 * 应用事件监听接口，用于监听并处理应用中的事件传播  
 *  
 * @author jixu  
 * @title ApplicationListener  
 * @date 2025/5/19 19:40  
 */
 public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {  
  
    /**  
     * 当有事件发布时，会触发该方法执行  
     *  
     * @param event 接收一个ApplicationEvent对象，表示发布的事件  
     */  
    void onApplicationEvent(E event);  
}
```

### 3，事件发布

#ApplicationEventPublisher

该接口会被ApplicationContext实现，当调用该方法发布一个事件时，对应的事件监听器就会在已缓存的事件监听者列表当中查询监听了该事件的监听者，并执行对应逻辑。

```java
/**  
 * ApplicationEventPublisher接口用于发布应用事件  
 * 它允许将特定的事件通知给所有感兴趣的监听器  
 * 主要用于实现应用内部的事件驱动架构  
 *   
 * @author jixu  
 * @title ApplicationEventPublisher  
 * @date 2025/5/19 20:59  
 */
 public interface ApplicationEventPublisher {  
  
    /**  
     * 发布一个应用事件  
     *   
	 * @param event 要发布的应用事件实例，不能为空，该事件将被传递给所有注册的监听器  
     */  
    void publishEvent(ApplicationEvent event);  
}
```

### 4，事件监听器

事件监听器是用来统筹所有事件监听者与事件的调用

#ApplicationEventMulticaster

```java
/**  
 * 事件广播器接口，用于管理监听器的注册和事件的广播  
 *  
 * @author jixu  
 * @title ApplicationEventMulticaster  
 * @date 2025/5/19 19:44  
 * */
 public interface ApplicationEventMulticaster  {  
  
    /**  
     * 添加一个应用事件监听器  
     *  
     * @param listener 要添加的监听器  
     */  
    public void addApplicationListener(ApplicationListener<?> listener);  
  
  
    /**  
     * 移除一个应用事件监听器  
     *  
     * @param listener 要移除的监听器  
     */  
    public void removeApplicationListener(ApplicationListener<?> listener);  
  
    /**  
     * 广播指定的应用程序事件到所有已注册的监听器  
     *  
     * @param event 要广播的事件  
     */  
    public void multicastEvent(ApplicationEvent event);  
}
```

#AbstractApplicationEventMulticaster

```java
/**
 * 抽象的应用程序事件多播器类，负责管理应用程序事件的广播
 * 它实现了ApplicationEventMulticaster和BeanFactoryAware接口
 * 
 * @author jixu
 * @title AbstractApplicationEventMulticaster
 * @date 2025/5/19 19:45
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster , BeanFactoryAware {

    // 定义一个Set集合用于保存所有的事件监听者
    protected final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

    // 保存BeanFactory的引用，以便于访问BeanFactory中的资源
    protected BeanFactory beanFactory;

    /**
     * 设置BeanFactory引用
     * 
     * @param beanFactory BeanFactory实例
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 添加应用程序事件监听器
     * 
     * @param listener 要添加的事件监听器
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    /**
     * 移除应用程序事件监听器
     * 
     * @param listener 要移除的事件监听器
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove((ApplicationListener<ApplicationEvent>) listener);
    }
}

```

#SimpleApplicationEventMulticaster

```java
/**  
 * SimpleApplicationEventMulticaster类用于处理应用事件的多播  
 * 它继承自AbstractApplicationEventMulticaster，实现了事件多播的具体逻辑  
 *  
 * @author jixu  
 * @title SimpleApplicationEventMulticaster  
 * @date 2025/5/19 19:59  
 */
 public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{  
  
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){  
        super.beanFactory = beanFactory;  
    }  
  
    /**  
     * 广播指定的应用程序事件到所有已注册的监听器  
     *  
     * @param event 要广播的事件  
     */  
    @Override  
    public void multicastEvent(ApplicationEvent event) {  
        // 循环获取当前所有的事件监听者  
        for (ApplicationListener<ApplicationEvent> applicationListener : super.applicationListeners) {  
            if (supportsEvent(applicationListener,event)){  
                // 监听到事件发布，执行对应逻辑  
                applicationListener.onApplicationEvent(event);  
            }  
        }  
    }  
  
    /**  
     * 判断指定的事件监听器是否支持处理给定的应用程序事件  
     *  
     * @param applicationListener 事件监听器  
     * @param event 应用程序事件  
     * @return 如果监听器支持处理事件，则返回true；否则返回false  
     */    
     protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener,ApplicationEvent event){  
        // 获取到applicationListener实现的第一个接口  
        Type type = applicationListener.getClass().getGenericInterfaces()[0];  
        // 获取到接口当中的泛型参数类型 --》 具体的ApplicationEvent  
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];  
        // 获取到具体的ApplicationEvent对应的类型名称  
        String className = actualTypeArgument.getTypeName();  
  
        Class<?> eventClassName;  
  
        try {  
            eventClassName = Class.forName(className);  
        } catch (ClassNotFoundException e) {  
            throw new BeansException(String.format("事件名称：【%s】错误",className));  
        }  
  
        // 判断当前Event与目标Event是否相同  
        return eventClassName.isAssignableFrom(event.getClass());  
  
    }  
}
```

其实这里是典型的分为三层，在Spring当中很多地方的逻辑也都是这样处理的，使用这种方式可以使代码更加灵活

### 5，修改应用上下文

其实到这里我们的事件监听器就基本完成了，现在只需要在ApplicationContext当中进行初始化就可以使用了

#AbstractApplicationContext


```java
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
  
    // 初始化事件发布器  
    initApplicationEventMulticaster();  
  
    // 注册事件监听器  
    registerListeners();  
  
    // 提前初始化单列Bean  
    beanFactory.preInstantiateSingletons();  
  
    // 发布容器刷新完成事件，通知实现了ContextRefreshedEvent  
    finishRefresh();  
  
  
}
```

初始化SimpleApplicationEventMulticaster，将ApplicationListener子类即其所有监听者加入到容器当中

```java
/**  
 * 初始化事件监听器applicationEventMulticaster  
 */
private void initApplicationEventMulticaster() {  
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();  
    applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);  
    beanFactory.addSingletonBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME,applicationEventMulticaster);  
}  
  
  
/**  
 *  将ApplicationListener的子类事件监听者加入到对应容器当中  
 */  
private void registerListeners() {  
    Collection<ApplicationListener> applicationListeners = getBeanOfType(ApplicationListener.class).values();  
    for (ApplicationListener applicationListener : applicationListeners) {  
        this.applicationEventMulticaster.addApplicationListener(applicationListener);  
    }  
}
```

重写事件发布逻辑

```java
/**
 * 发布事件
 * 
 * @param event 待发布的应用事件，不能为空
 */
@Override
public void publishEvent(ApplicationEvent event) {
    // 将事件委托给应用事件多路广播器进行广播
    applicationEventMulticaster.multicastEvent(event);
}

```

### 6，自定义事件

```java
/**  
 * @author jixu  
 * @title CustomEvent  
 * @date 2025/5/19 21:39  
 */
public class CustomEvent extends ApplicationEvent {  
    /**  
     * Constructs a prototypical Event.     *     * @param source The object on which the Event initially occurred.  
     * @throws IllegalArgumentException if source is null.  
     */    public CustomEvent(Object source) {  
        super(source);  
    }  
}
```

```java
/**  
 * @author jixu  
 * @title CustomEventListener  
 * @date 2025/5/19 21:40  
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {  
  
  
    /**  
     * 当有事件发布时，会触发该方法执行  
     *  
     * @param event 接收一个ApplicationEvent对象，表示发布的事件  
     */  
    @Override  
    public void onApplicationEvent(CustomEvent event) {  
        System.out.println(this.getClass().getName());  
    }  
}
```


```java
@Test  
public void testApplicationCustomEvent(){  
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-event.xml");  
    applicationContext.publishEvent(new CustomEvent(applicationContext));  
}
```


### 7，容器刷新以及关闭事件监听

容器的刷新与关闭监听逻辑非常简单，只需要定义对应事件，之后在容器refresh与close逻辑执行完毕后发布对应事件即可

```java
/**  
 * 事件刷新类  
 *  
 * @author jixu  
 * @title ContextRefreshedEvent  
 * @date 2025/5/19 17:24  
 */public class ContextRefreshedEvent extends ApplicationContextEvent{  
  
    /**  
     * Constructs a prototypical Event.     *     * @param source The object on which the Event initially occurred.  
     * @throws IllegalArgumentException if source is null.  
     */    public ContextRefreshedEvent(Object source) {  
        super(source);  
    }  
}
```

```java
/**  
 * 发布容器刷新完成事件，通知实现了ContextRefreshedEvent  
 */
 private void finishRefresh() {  
    publishEvent(new ContextRefreshedEvent(this));  
}
```

```java
/**  
 * 事件关闭类  
 *  
 * @author jixu  
 * @title ContextCloseEvent  
 * @date 2025/5/19 17:23  
 */public class ContextCloseEvent extends ApplicationContextEvent{  
    /**  
     * Constructs a prototypical Event.     *     * @param source The object on which the Event initially occurred.  
     * @throws IllegalArgumentException if source is null.  
     */    public ContextCloseEvent(Object source) {  
        super(source);  
    }  
}
```

```java
private void doClose() {  
    // 发布容器关闭通知  
    publishEvent(new ContextCloseEvent(this));  
  
    // 销毁Bean  
    destroyBeans();  
  
}
```