
在前面的章节当中介绍完毕了ApplicationContext，也就是应用上下文。我们说BeanFactory是面向开发者的，ApplicationContext才是面向使用者的，实际上也是如此。在ApplicationContest当中我们对BeanFactory进行了进一步的集成，实现了自动化的XML文件读取，注册BeanFacoryPostProcess，注册BeanPostProcess，以及提前初始化单列Bean。

以上都是属于Bean生命周期的一部分，是通过Spring自动管理的，除此之外Spring还允许用户自定义初始化与销毁方法，同样也可以通过Spring自动化调用。

## 一，Bean的初始化

在实现初始化与销毁之前我们需要先考虑一下什么时候进行初始化，什么时候要销毁。
很简单，初始化发生在创建Bean的时候，而销毁发生在容器或者虚拟机关闭的时候。那么对于初始化来说他是针对对应的Bean的，也就是说在创建的时候执行，而销毁是一个统一的行为，当容器关闭时候需要对指定Bean进行统一的销毁。

那么明白了上述逻辑后我们就开始我们的逻辑实现

首先容器的初始化行为是正对Bean本身，实际上和BeanPostProcessor非常类似，只不过初始化的行为更具象罢了。

首先我们需要在BeanDefinition当中添加初始化的方法名，该名称用于用户通过XML/注解方式自定义初始化的方法

```java
private String initMethodName;
```

第二步，定义用于初始化的父类，这样我们的Bean就可以集成该类，重写初始化方法。

```java
public interface InitializingBean {  
    void afterPropertiesSet();  
}
```

现在再回到AbstractAutowireCapableBeanFactory当中的初始化方法initializeBean当中，在这里执行完了BeanPostProcess的前置增强之后，执行用户定义的初始化方法。

```java
private void initializeBean(String beanName, Object bean,BeanDefinition beanDefinition) {  
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

初始化的逻辑也很简单，首先判断用户是否实现了InitializingBean这个接口，如果实现了则调用其重写的afterPropertiesSet初始化方法。之后再判断用户是否是自己指定了自定义的初始化方法，如果指定则通过反射获取到该方法并执行即可。

```java
private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception{  
    if (bean instanceof InitializingBean){  
        ((InitializingBean) bean).afterPropertiesSet();  
    }  
  
    // 处理用户自定义的初始化方法  
    if (StrUtil.isNotEmpty(beanDefinition.getInitMethodName())){  
        Method initMethod = ClassUtil.getPublicMethod(bean.getClass(), beanDefinition.getInitMethodName());  
        if (initMethod == null) {  
            throw new BeansException(String.format("在Bean：%s 当中找不到名为：%s 的初始化方法",beanName,beanDefinition.getInitMethodName()));  
        }  
        initMethod.invoke(bean);  
    }  
}
```


## 二，Bean的销毁

Bean的销毁相对于初始化来说最大的区别在于，初始化时正对单个Bean，销毁是针对所有要销毁的Bean。
也就是说，我们需要定一个Map集合存储所有要销毁的Bean，在容器关闭时遍历集合，执行其销毁方法。

首先我们需要在BeanDefinition当中添加销毁的方法名，该名称用于用户通过XML/注解方式自定义销毁的方法

```java
private String destroyMethodName;
```

第二步，定义用于销毁的父类，这样我们的Bean就可以集成该类，重写销毁方法。

```java
public interface DisposableBean {  
  
    public void destroy();  
}
```

然后我们需要回到ConfigurableApplicationContext，因为实际上Bean的销毁时应用上下文的行为，在我们关闭ApplicationContext的时候才会执行。

```java
public interface ConfigurableApplicationContext extends ApplicationContext {  
    /**  
     * 刷新容器，重新加载并初始化所有配置和Bean  
     */    
    void refresh();  
  
    /**  
     * 关闭ApplicationContext  
     */    
    void close();  
  
  
    void registerShutdownHook();  
}
```

在AbstractApplicationContext实现这些方法，先看下面的代码，这里可能会有一个疑问就是getBeanFactory().destroySingletons();  通过完整的代码我们可以知道getBeanFactory()返回的是一个DefaultListableBeanFactory的对象，那么我们也要在其父类当中实现该方法。

```java
/**  
 * 关闭ApplicationContext  
 */@Override  
public void close() {  
    doClose();  
}  
  
private void doClose() {  
    destroyBeans();  
  
}  
  
private void destroyBeans() {  
    getBeanFactory().destroySingletons();  
}  
  
@Override  
public void registerShutdownHook() {  
    Thread shutdownHook = new Thread(this::doClose);  
    Runtime.getRuntime().addShutdownHook(shutdownHook);  
}
```

首先我们要明确一点，在Spring当中所有接口都被指责化了，也就是说不同接口有着不同的功能，而Bean的销毁也属于单列Bean的生命周期当中，所以我们要在DefaultSingletonBeanRegistry当中实现该方法，除此之外destroySingletons该方法我们还需要在ConfigurableBeanFactory当中定义。
其中ConfigurableBeanFactory与DefaultSingletonBeanRegistry之间都继承于SingletonBeanRegistry，但是我们不在SingletonBeanRegistry当中定义destroySingletons接口，二者时通过组合继承实现的

```java
public void destroySingletons(){  
    Set<String> beanNames = disposableBeans.keySet();  
    for (String beanName : beanNames) {  
        DisposableBean disposableBean = disposableBeans.remove(beanName);  
        // 执行销毁方法  
        disposableBean.destroy();  
    }  
  
}
```

```java
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {  
  
    /**  
     * 添加一个 BeanPostProcessor 到工厂中。  
     * BeanPostProcessor 可以在 Bean 初始化前后执行自定义逻辑。  
     *  
     * @param beanPostProcessor 要添加的 BeanPostProcessor 实例  
     */  
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);  
  
  
    /**  
     * 销毁单例bean  
     */    
    void destroySingletons();  
}
```

其实到这里所有的逻辑就结束了，下面写一个测试

```java
package org.qlspringframework.beans.ioc.bean;  
  
import org.qlspringframework.beans.factory.DisposableBean;  
import org.qlspringframework.beans.factory.InitializingBean;  
  
/**  
 * @author jixu  
 * @title People  
 * @date 2025/4/7 09:54  
 */public class People implements DisposableBean, InitializingBean {  
  
    private String name;  
    private Integer age;  
  
    private Car car;  
  
    public Car getCar() {  
        return car;  
    }  
  
    public void setCar(Car car) {  
        this.car = car;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public Integer getAge() {  
        return age;  
    }  
  
    public void setAge(Integer age) {  
        this.age = age;  
    }  
  
    @Override  
    public String toString() {  
        return "People{" +  
                "name='" + name + '\'' +  
                ", age=" + age +  
                ", car=" + car +  
                '}';  
    }  
  
    @Override  
    public void destroy() {  
        System.out.println("People destroy");  
    }  
  
    @Override  
    public void afterPropertiesSet() {  
        System.out.println("People init");  
    }  
}
```

```java
public class InitAndDestroyMethodTest {  
  
    @Test  
    public void testInitAndDestroy(){  
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");  
        classPathXmlApplicationContext.registerShutdownHook();  
    }  
}
```

## 三，当前Bean的生命周期

![[../../assets/Pasted image 20250504165747.png]]