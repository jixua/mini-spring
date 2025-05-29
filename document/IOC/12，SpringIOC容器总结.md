到这里其实Spring的IOC功能已经基本实现了，现在我们从全局的角度来看一下Bean的生命周期

1. 定义XML文件
2. 初始化BeanDefinition
3. 将BeanPostProcess加入到对应容器当中，在后续doCreateBean到时候调用
4. 调用BeanFactoryPostProcess修改指定的BeanDefinition
5. 提前初始化所有的单例Bean

以上全是由ApplicationContext完成

1. 通过策略模式，调用对应的实例化逻辑（对于普通Bean使用反射创建Bean）
2. applyPropertyValues属性赋值
3. 执行初始化逻辑
4. 执行applyBeanPostProcessorsBeforeInitialization的前置处理
5. 调用初始化方法 （继承InitializingBean重写afterPropertiesSet或者指定初始化方法时生效）
6. 执行applyBeanPostProcessorsAfterInitialization的后置处理
7. 注册销毁方法（继承DisposableBean重写destroy方法或者指定销毁方法时生效）
8. 在关闭容器时执行销毁方法

以上部分由BeanFactory完成，属于初始化单例Bean的子步骤

![[../../assets/Pasted image 20250513211808.png]]
![[../../assets/Pasted image 20250513212144.png]]


这里先来聊一下什么是Spring的IOC容器吧

对于Spring来说IOC就是它的核心，像我们常说的IOC，AOP以及事务等，实际上也都是基于IOC的postProcess实现的。IOC实际上就是一个容器，那么容器是用来存放东西的，在Spring当中就是用来存放类的，至于容器的类型有很多，比如说存放BeanDefinition，存放SingletonBean等等，就不多说了。
那么除此之外我们还需要知道一件事对于IOC来说并不是单单指的是BeanFactory，实际上包含了BeanFactory与ApplicationContext。对于BeanFactory来说它更像是提供给开发人员使用的，下面一个简单的案例演示一下

```java
public void testXmlResourceReader(){  
    DefaultListableBeanFactory factory = new DefaultListableBeanFactory();  
    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);  
    xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");  
    People person = (People) factory.getBean("people");  
    System.out.println(person);  
}
```

我们需要手动去


接下来我们就从架构设计的角度来聊一聊IOC的实现

首先来看一下完整的类图
![[../../assets/Pasted image 20250516204019.png]]

对于Spring的架构设计是极其复杂的，上述也是只包含了BeanFactory的相关实现，我们可以看到`DefaultListableBeanFactory`是BeanFactory的核心类，那么除此之外的话，Spring对单一职责的设计也做到了极致，BeanFactory的每一个子接口实际上都对应着不同的功能。

