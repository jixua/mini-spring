
在前面的章节当中我们实现了SpringIOC的基本功能，但是这还不够，可以看一下下面的列子

```java
@Test  
public void testBeanPostPostProcess() {  
    // 创建默认的BeanFactory实例  
    DefaultListableBeanFactory factory = new DefaultListableBeanFactory();  
  
    // 创建XML Bean定义读取器，并加载指定的Spring配置文件  
    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);  
    xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");  
  
    // 向BeanFactory注册自定义的BeanPostProcessor  
    factory.addBeanPostProcessor(new CustomerBeanPostProcessor());  
  
    // 从BeanFactory中获取名为"people"的Bean实例，并打印其内容  
    People people = (People) factory.getBean("people");  
    System.out.println(people);  
}
```

在这里我们创建了一个BeanFactory对象之后还需要，手动调用方法加载资源文件，同时对于PostProcess也需要我们通过内置方法进行添加，那能否实现自动化的添加就是ApplicationContext要做的事情



其实ApplicationContext的实现与BeanFactory非常类似，都是通过对子类的委托实现功能的拓展。

再说实现ApplicationContext代码之前，我们先想想要做什么，如何实现自动化的对Bean的初始化，要经过哪些步骤

首先我们在初识Spring的时候肯定都了解过一个类`ClassPathXmlApplicationContext`可以看一下下面的代码

```java
	@Test
	public void testApplicationContext() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
		Person person = applicationContext.getBean("person", Person.class);
	}
```

我们通过`ClassPathXmlApplicationContext`这个类的构造函数传入要加载的xml文件从classPath路径，之后我们就可以从容器当中获取到我们的Bean了

那么在整个历程要经过哪些步骤呢

1. 通过ClassLoader加载xml资源文件，获取到Resource对象，然后将其通过XmlBeanDefinitionReader将里面的标签注册为BeanDefinition。
2. 执行BeanFactoryPostProcess
3. 将BeanPostProcess加入到对应的容器当中
4. 提前初始化Bean

### 一，对XMl资源文件的解析

在Spring当中对XML文件的加载委托给了XmlBeanDefinitionReader这个类，实际上对于BeanDefinitionReader当中就提供了基本的加载资源文件的接口，其抽象实现AbstractBeanDefinitionReader，对当中的基本逻辑进行了补充。在此之后我们就可以自定义不同文件的加载逻辑去加载为BeanDefinition，值得说一下的是BeanDefinitionReader的加载beanDefinition的方法loadBeanDefinitions在实际外部调用的过程当中传入的是资源文件的ClassPath路径，因此在这里还需要依赖到ResourceLoader去通过解析路径。

上面是对XML文件加载的回顾，经过上述我们也可以明白，要实现自动化的文件加载需要依赖到哪些类，现在我们只需要直接拿出来用即可

来看一下实现逻辑

首先我们得定义好我的ApplicationContext，它也就是一个接口实现逻辑与BeanFactory，都将单一职责的原则贯彻到底，具体的功能拓展都委托给子类

```java
/**  
 * ApplicationContext 接口是 Spring 框架中的核心接口之一，用于提供应用程序的配置信息。  
 * 它继承了 ListableBeanFactory、HierarchicalBeanFactory 和 ResourceLoader 接口，  
 * 从而具备了以下功能：  
 * 1. 列出所有 Bean 定义的能力（通过 ListableBeanFactory）。  
 * 2. 支持 Bean 工厂的层次结构（通过 HierarchicalBeanFactory）。  
 * 3. 加载资源文件的能力（通过 ResourceLoader）。  
 *  
 * 该接口通常用于在 Spring 应用程序中获取 Bean 实例、管理 Bean 的生命周期以及加载资源。  
 */  
public interface ApplicationContext extends ListableBeanFactory , HierarchicalBeanFactory  {  
      
}
```

`ConfigurableApplicationContext`实现ApplicationContext的配置化功能

```java
/**  
 * ConfigurableApplicationContext 接口扩展了 ApplicationContext 接口，提供了对应用上下文配置的支持。  
 * 该接口允许在应用上下文初始化之前或之后进行配置，例如设置父上下文、刷新上下文等操作。  
 *  
 * 实现该接口的类通常用于管理 Spring 应用上下文的生命周期和配置。  
 *  
 * @see ApplicationContext */

public interface ConfigurableApplicationContext extends ApplicationContext {  
    /**  
     * 刷新容器。  
     * 该方法用于重新加载或更新容器中的内容，通常用于在容器状态发生变化时，  
     * 确保容器内的数据或配置与当前状态保持一致。  
     * 该方法不接收任何参数，也不返回任何值。  
     */  
    void refresh();  
}
```

`AbstractApplicationContext`，在上面的介绍当中也说了，首先我们要做的事情就是解析XML文件初始化Bean，所以我们在`AbstractApplicationContext`当中的refresh方法当中开始对该逻辑进行实现。但是在这里就还会有一个问题，当前我们的需求是解析XML文件，那如果在后续我们相对其进行拓展我想解析JSON注册Bean怎么办。这里也就是为什么在`AbstractApplicationContext`当中调用的都是抽象方法，在这里我们实际上就只是定义一个模版，规范解析的步骤，至于具体如何实现就需要根据我们的需求了。这样就可以实现不同解析方式的动态扩展，实现高内聚低耦合。

```java
  
/**  
 * AbstractApplicationContext 是一个抽象类，实现了 ConfigurableApplicationContext 接口。  
 * 该类提供了应用程序上下文的基本实现，特别是容器的刷新功能。  
 *  
 * @author: jixu 
 * @create: 2025-04-19 14:37 
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {  
  
    /**  
     * 刷新容器。  
     * 该方法用于重新加载或刷新应用程序上下文中的配置和资源。  
     * 通常用于在运行时动态更新应用程序的配置。  
     *  
     * 该方法是一个抽象方法的实现，具体刷新逻辑由子类提供。  
     */  
    @Override  
    public void refresh() {  
        // 通过子类创建BeanFactory，同时初始化beanDefinition  
        refreshBeanFactory();  
  
        // 获取到Bean工厂  
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();  
    }  
 
  

    protected abstract void refreshBeanFactory();  
    
	protected abstract ConfigurableListableBeanFactory  getBeanFactory();
  
}
```

`AbstractRefreshableApplicationContext`，在实现完`AbstractApplicationContext`的逻辑后我们发现在`refreshBeanFactory`也就是初始化Bean的方法当中有些逻辑是共通的，不管我们定义何种解析方法都需要用到这些东西，那么我们就可以在这里进行进一步的抽取。

```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{  
  
    private ConfigurableListableBeanFactory  beanFactory;  

  
    @Override  
    protected void refreshBeanFactory() {  
        // 创建Bean  
        DefaultListableBeanFactory beanFactory = createBeanFactory();  
        // 加载BeanDefinition  
        loadBeanDefinitions(beanFactory);  
        this.beanFactory = beanFactory;  
    }  
  
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) ;  
  
    private DefaultListableBeanFactory createBeanFactory() {  
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();  
        return defaultListableBeanFactory;  
    }  
  
  
    @Override  
    public ConfigurableListableBeanFactory getBeanFactory() {  
        return beanFactory;  
    }  
  
    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {  
        this.beanFactory = beanFactory;  
    }  
  
  
}
```

`AbstractXmlApplicationContext`现在就来到真正的XML文件的解析逻辑了，可以看到在这里我的定义了一个AbstractXmlApplicationContext，那么是不是也可以定义一个AbstractJSONApplicationContext实现AbstractRefreshableApplicationContext的loadBeanDefinitions方法呢，答案是肯定的。但是只做到这一步还是不够的，我们需要解析XML文件就需要获取到文件的路径，这个路径的获取通过上面的示例也能看出来，是通过构造方法创建的，那我我们就还需要一个用于获取路径的类实现AbstractXmlApplicationContext同时实现获取路径的方法

```java
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{  
  
  
    @Override  
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {  
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);  
        String[] locations = getConfigLocations();  
        xmlBeanDefinitionReader.loadBeanDefinitions(locations);  
    }  
  
    // 获取到Classpath下的所有配置资源路径  
    protected abstract String[] getConfigLocations();  
}
```

`ClassPathApplicationContext`

```java
public class ClassPathApplicationContext extends AbstractXmlApplicationContext{  
  
    private String[] configLocations;  
  
    @Override  
    protected String[] getConfigLocations() {  
        return this.configLocations;  
    }  
  
  
    public ClassPathApplicationContext(String configLocation){  
        this.configLocations = new String[]{configLocation};  
    }  
  
    public ClassPathApplicationContext(String[] configLocations){  
        this.configLocations = configLocations;  
    }  
  
}
```

到这里我们的第一步对于XML的解析就讲完了，后面应该就是执行BeanPostProcessors与BeanFactoryPostProcess的增强方法了

### 二，BeanFactoryPostProcess的自动化执行

首先我们先来回顾一下BeanFactoryPostProcess的实现原理，BeanFactoryPostProcessor的实现是基于Spring的一个扩展点`ConfigurableListableBeanFactory`，在该接口当中我们可以通过其内置的方法`getBeanDefinition`获取到指定Bean的定义信息之后就可以对其进行修改。除此之外执行增强方法也需要调用有具体子类实现的`postProcessBeanFactory`方法。

那么现在就会产生一个问题，如何拿到所有已定义的BeanFactoryPostProcess。其实逻辑很简单，获取到所有的BeanFactoryPostProcess，之后循环遍历执行即可。Spring是通过将所有的BeanFactoryPostProcess加入到容器当中，然后从容器获取BeanFactoryPostProcess类型的Bean，来解决这个问题的。

```java
// 执行BeanFactoryPostProcess的方法  
invokeBeanFactoryPostProcessors(beanFactory);
```

```java
/**  
 * 执行所有BeanFactoryPostProcessor的postProcessBeanFactory方法。  
 * 该方法会从BeanFactory中获取所有类型为BeanFactoryPostProcessor的Bean，并依次调用它们的postProcessBeanFactory方法。  
 *  
 * @param beanFactory 可配置的BeanFactory实例  
 */
private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {  
    // 获取到所有已注册到容器当中的BeanPostProcess  
    Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeanOfType(BeanFactoryPostProcessor.class);  
    for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {  
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);  
    }  
}
```


### 三，BeanPostProcess的自动化执行

BeanPostProcess的逻辑与BeanFactoryPostProcess非常类似，只是区别在于BeanPostProcess是对Bean的增强其前置与后置增强方法不需要我们手动调用，在createBean的时候会自动调用这些方法。我们要做的就是将用户已定义的BeanPostProcess加入到对应的BeanPostProcessMap当中。

```java
// 注册BeanPostPostProcess  
registerBeanPostProcessors(beanFactory);
```

```java
/**  
 * 注册所有BeanPostProcessor到BeanFactory中。  
 * 该方法会从BeanFactory中获取所有类型为BeanPostProcessor的Bean，并将它们注册到BeanFactory中。  
 *  
 * @param beanFactory 可配置的BeanFactory实例  
 */  
private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {  
    Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeanOfType(BeanPostProcessor.class);  
    for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {  
        beanFactory.addBeanPostProcessor(beanPostProcessor);  
    }  
}
```

### 四，提前初始化单列Bean

preInstantiateSingletons方法是ConfigurableListableBeanFactory提供的，我们在AbstractApplicationContext当中已经继承了该方法，直接调用即可

```java
// 提前初始化单列Bean  
beanFactory.preInstantiateSingletons();
```


### 五，完整代码

可参考 https://gitee.com/jixuonline/jixu-mini-spring.git

![](../assets/Pasted%20image%2020250429101356.png)


`ApplicationContext`

```java 
/**  
 * ApplicationContext 接口是 Spring 框架中的核心接口之一，用于提供应用程序的配置信息。  
 * 它继承了 ListableBeanFactory、HierarchicalBeanFactory 和 ResourceLoader 接口，  
 * 从而具备了以下功能：  
 * 1. 列出所有 Bean 定义的能力（通过 ListableBeanFactory）。  
 * 2. 支持 Bean 工厂的层次结构（通过 HierarchicalBeanFactory）。  
 * 3. 加载资源文件的能力（通过 ResourceLoader）。  
 *  
 * 该接口通常用于在 Spring 应用程序中获取 Bean 实例、管理 Bean 的生命周期以及加载资源。  
 */  
public interface ApplicationContext extends ListableBeanFactory , HierarchicalBeanFactory  {  
}
```

`ConfigurableApplicationContext`

```java
/**  
 * ConfigurableApplicationContext 接口扩展了 ApplicationContext 接口，提供了对应用上下文配置的支持。  
 * 该接口允许在应用上下文初始化之前或之后进行配置，例如设置父上下文、刷新上下文等操作。  
 *  
 * 实现该接口的类通常用于管理 Spring 应用上下文的生命周期和配置。  
 *  
 * @see ApplicationContext */
 public interface ConfigurableApplicationContext extends ApplicationContext {  
    /**  
     * 刷新容器。  
     * 该方法用于重新加载或更新容器中的内容，通常用于在容器状态发生变化时，  
     * 确保容器内的数据或配置与当前状态保持一致。  
     * 该方法不接收任何参数，也不返回任何值。  
     */  
    void refresh();  
}
```

`AbstractApplicationContext`

```java
/**  
 * AbstractApplicationContext 是一个抽象类，实现了 ConfigurableApplicationContext 接口。  
 * 该类提供了应用程序上下文的基本实现，特别是容器的刷新功能。  
 *  
 * @author: jixu 
 * @create: 2025-04-19 14:37
 */
 public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {  
  
    /**  
     * 刷新容器。  
     * 该方法用于重新加载或刷新应用程序上下文中的配置和资源。  
     * 通常用于在运行时动态更新应用程序的配置。  
     *  
     * 该方法是一个抽象方法的实现，具体刷新逻辑由子类提供。  
     */  
    @Override  
    public void refresh() {  
        // 通过子类创建BeanFactory，同时初始化beanDefinition  
        refreshBeanFactory();  
  
        // 获取到Bean工厂  
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();  
  
        // 执行BeanFactoryPostProcess的方法  
        invokeBeanFactoryPostProcessors(beanFactory);  
  
        // 注册BeanPostPostProcess  
        registerBeanPostProcessors(beanFactory);  
  
        // 提前初始化单列Bean  
        beanFactory.preInstantiateSingletons();  
  
    }  
  
    /**  
     * 注册所有BeanPostProcessor到BeanFactory中。  
     * 该方法会从BeanFactory中获取所有类型为BeanPostProcessor的Bean，并将它们注册到BeanFactory中。  
     *  
     * @param beanFactory 可配置的BeanFactory实例  
     */  
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {  
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeanOfType(BeanPostProcessor.class);  
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {  
            beanFactory.addBeanPostProcessor(beanPostProcessor);  
        }  
    }  
  
    /**  
     * 执行所有BeanFactoryPostProcessor的postProcessBeanFactory方法。  
     * 该方法会从BeanFactory中获取所有类型为BeanFactoryPostProcessor的Bean，并依次调用它们的postProcessBeanFactory方法。  
     *  
     * @param beanFactory 可配置的BeanFactory实例  
     */  
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {  
        // 获取到所有已注册到容器当中的BeanPostProcess  
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeanOfType(BeanFactoryPostProcessor.class);  
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {  
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);  
        }  
  
    }  
  
    /**  
     * 刷新BeanFactory。  
     * 该方法是一个抽象方法，具体实现由子类提供，用于创建或刷新BeanFactory。  
     */  
    protected abstract void refreshBeanFactory();  
  
    /**  
     * 获取当前BeanFactory实例。  
     * 该方法是一个抽象方法，具体实现由子类提供，用于返回当前BeanFactory的实例。  
     *  
     * @return 当前BeanFactory的实例  
     */  
    protected abstract ConfigurableListableBeanFactory  getBeanFactory();  
  
    /**  
     * 根据指定的类型获取所有符合条件的Bean实例，并以Map形式返回。  
     * Map的键为Bean的名称，值为对应的Bean实例。  
     *  
     * @param type 要查找的Bean类型  
     * @return 包含所有符合类型条件的Bean实例的Map，键为Bean名称，值为Bean实例  
     */  
    @Override  
    public <T> Map<String, T> getBeanOfType(Class<T> type) {  
        return getBeanFactory().getBeanOfType(type);  
    }  
  
    /**  
     * 获取当前BeanFactory中所有Bean定义的名称。  
     *  
     * @return 包含所有Bean定义名称的字符串数组  
     */  
    @Override  
    public String[] getBeanDefinitionNames() {  
        return getBeanFactory().getBeanDefinitionNames();  
    }  
  
    /**  
     * 根据指定的 Bean 名称获取对应的 Bean 实例。  
     *  
     * @param name Bean 的名称，通常是在 Spring 配置文件中定义的 Bean 的 ID 或名称。  
     * @return 返回与指定名称对应的 Bean 实例。如果找不到对应的 Bean，可能会抛出异常。  
     */  
    @Override  
    public Object getBean(String name) {  
        return getBeanFactory().getBean(name);  
    }  
}
```

`AbstractRefreshableApplicationContext`

```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{  
  
  
  
    private ConfigurableListableBeanFactory  beanFactory;  
  
  
    @Override  
    protected void refreshBeanFactory() {  
        // 创建Bean  
        DefaultListableBeanFactory beanFactory = createBeanFactory();  
        // 加载BeanDefinition  
        loadBeanDefinitions(beanFactory);  
        this.beanFactory = beanFactory;  
    }  
  
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) ;  
  
    private DefaultListableBeanFactory createBeanFactory() {  
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();  
        return defaultListableBeanFactory;  
    }  
  
  
    @Override  
    public ConfigurableListableBeanFactory getBeanFactory() {  
        return beanFactory;  
    }  
  
    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {  
        this.beanFactory = beanFactory;  
    }  
  
  
}
```

`AbstractXmlApplicationContext`

```java
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{  
  
  
    @Override  
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {  
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);  
        String[] locations = getConfigLocations();  
        xmlBeanDefinitionReader.loadBeanDefinitions(locations);  
    }  
  
    // 获取到Classpath下的所有配置资源路径  
    protected abstract String[] getConfigLocations();  
}
```

`ClassPathApplicationContext`

```java
public class ClassPathApplicationContext extends AbstractXmlApplicationContext{  
  
    private String[] configLocations;  
  
    @Override  
    protected String[] getConfigLocations() {  
        return this.configLocations;  
    }  
  
  
    public ClassPathApplicationContext(String configLocation){  
        this.configLocations = new String[]{configLocation};  
    }  
  
    public ClassPathApplicationContext(String[] configLocations){  
        this.configLocations = configLocations;  
    }  
  
}
```