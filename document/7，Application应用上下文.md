
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


## ApplicationContext实现原理

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

### 对XMl资源文件的解析

在Spring当中对XML文件的加载委托给了XmlBeanDefinitionReader这个类，实际上对于BeanDefinitionReader当中就提供了基本的加载资源文件的接口，其抽象实现AbstractBeanDefinitionReader，对当中的基本逻辑进行了补充。在此之后我们就可以自定义不同文件的加载逻辑去加载为BeanDefinition，值得说一下的是BeanDefinitionReader的加载beanDefinition的方法loadBeanDefinitions在实际外部调用的过程当中传入的是资源文件的ClassPath路径，因此在这里还需要依赖到ResourceLoader去通过解析路径。

上面是对XML文件加载的回顾，经过上述我们也可以明白，要实现自动化的文件加载需要依赖到哪些类

