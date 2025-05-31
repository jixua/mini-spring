
众所周知在Spring的Bean当中是存在两种作用域的，即单例模式与多例模式，可通过scope来指定

下面就是注册一个多例Bean

```java
<bean id="people" class="org.qlspringframework.beans.ioc.bean.People" scope="prototype">  
    <property name="name" value="derek"/>  
</bean>
```

那么什么是单例模式什么是多例模式呢？说白了其实就是与是否通过Spring的完整生命周期有关，对于单例模式的Bean，会通过完整的生命周期来创建，同时也会将Bean加入到Spring的缓存当中，再次获取会从缓存当中拿。那么多例模式就不会产生这种情况，每一次通过get()方法获取 Bean的时候都是重新创建。

下面看一下如何实现scope作用域支持

首先Bean的作用域是通过BeanDefinition定义的，那我们就需要在其中定义相关属性，除此之外Bean的默认作用域是单例，也就是说在用户未指定的情况下应保持singleton。

`BeanDefinition`

```java
public static String SCOPE_SINGLETON = "singleton";  
  
public static String SCOPE_PROTOTYPE = "prototype";  
  
private String scope = SCOPE_SINGLETON;

  
// 单列模式  
private boolean singleton = true;  
  
// 多列模式  
private boolean prototype = false;  
  
public void setScope(String scope){  
    this.scope = scope;  
    this.singleton = SCOPE_SINGLETON.equals(scope);  
    this.prototype = SCOPE_PROTOTYPE.equals(scope);  
}  
  
public boolean isSingleton() {  
    return this.singleton;  
}  
  
public boolean isPrototype() {  
    return this.prototype;  
}
```

之后就是对XML的解析过程

`XmlBeanDefinitionReader`
```

```java
String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);  
  
beanDefinition.setDestroyMethodName(destroyMethodName);  
if (StrUtil.isNotEmpty(beanScope)) {  
    beanDefinition.setScope(beanScope);  
}
```

这样我们的基础准备就完毕了，剩下的是对执行Bean生命周期的过程进行修改

首先就是多例Bean在创建完毕后不需要加入缓存

`AbstractAutowireCapableBeanFactory`

```java
// 创建完毕后加入缓存  
if (beanDefinition.isSingleton()){  
    super.addSingletonBean(beanName, bean);  
}
```

其次对于多例Bean不需要提前初始化

`DefaultListableBeanFactory`

```java
/**  
 * 提前实例化所有单例Bean。  
 */  
@Override  
public void preInstantiateSingletons() {  
    beanDefinitionMap.forEach((key,value) -> {  
        if (value.isSingleton()){  
            super.getBean(key);  
        }  
    });  
}
```