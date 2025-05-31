
## 一，`FactoryBean` 的作用


FactoryBean与BeanFactory并不是一个东西，实质上FactoryBean只是Bean工厂当中的一种特殊Bean，在 Spring 框架中，`FactoryBean` 是一个非常特殊的接口，它的主要作用是**自定义 Bean 的实例化逻辑**，可以在 Spring 容器中代替传统的 `@Bean` 或 `XML` 中的 `<bean>` 标签提供复杂对象的创建方式。

在 Spring 框架中，`FactoryBean` 是一个非常特殊的接口，它的主要作用是**自定义 Bean 的实例化逻辑**，可以在 Spring 容器中代替传统的 `@Bean` 或 `XML` 中的 `<bean>` 标签提供复杂对象的创建方式。


### 1、使用方式

`FactoryBean<T>` 是一个泛型接口，你需要实现它的三个核心方法：

```java
public interface FactoryBean<T> {

    // 返回由 FactoryBean 创建的对象（即实际注入到容器中的 Bean）
    T getObject() throws Exception;

    // 返回创建的对象的类型
    Class<?> getObjectType();

    // 返回是否是单例，true 表示容器只创建一个实例
    boolean isSingleton();
}
```

---

### 2、使用场景示例

#### 创建代理对象（比如 MyBatis 中的 MapperFactoryBean）

```java
public class MyServiceProxyFactoryBean implements FactoryBean<MyService> {

    @Override
    public MyService getObject() throws Exception {
        // 创建一个代理对象
        return (MyService) Proxy.newProxyInstance(
                MyService.class.getClassLoader(),
                new Class[]{MyService.class},
                new MyServiceInvocationHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return MyService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

配置方式：

```xml
<bean id="myService" class="com.example.MyServiceProxyFactoryBean"/>
```

---

### 3、与普通 Bean 的区别

|普通 Bean|FactoryBean|
|---|---|
|直接由容器反射实例化|自定义创建逻辑|
|配置 `<bean class="XXX">` 后得到的是该类的实例|配置 `<bean class="XXXFactoryBean">` 后得到的是 `getObject()` 返回的实例|
|比较适合简单对象|更适合复杂、动态、第三方对象的创建|

---

### 4、特殊语法：`&` 前缀

- Spring 容器中 `FactoryBean` 本身也是一个 Bean。
    
- 若要获取 `FactoryBean` 自身而不是它生产的对象，需使用 `&` 前缀。
    

```java
ApplicationContext context = ...;
Object factoryBean = context.getBean("&myService");  // 获取的是 FactoryBean 实例
Object realBean = context.getBean("myService");     // 获取的是 getObject() 的返回值
```

---

### 5、典型应用

- MyBatis 的 `MapperFactoryBean`

- Spring AOP 的代理工厂

- Spring Cloud、Dubbo 的远程代理生成器

- 对第三方工厂模式的整合



## 二，底层实现

#BeanFactory

```java
/**  
 * FactoryBean接口定义了工厂方法模式的实现，用于在Spring框架中创建对象。  
 * 实现该接口的类可以自定义对象的创建过程，而不仅仅是通过构造器来创建。  
 *   
 * @author jixu  
 * @title FactoryBean  
 * @date 2025/5/20 14:00  
 */
public interface FactoryBean<T> {  
  
    /**  
     * 获取由FactoryBean创建的对象。  
     *   
	 * @return 创建的对象实例。  
     */  
    T getObject();  
  
    /**  
     * 判断由FactoryBean创建的对象是单例还是多例。  
     *   
	 * @return 如果对象是单例，则返回true；否则返回false。  
     */  
    boolean isSingleton();  
}
```

#AbstractBeanFactory

修改getBean的逻辑，这里实现了一个基本的FactoryBean的功能，其中对于获取FactoryBean自身还未实现

```java
public Object getBean(String beanName) {  
    // 尝试从缓存当中获取Bean  
    Object sharedInstance = super.getSingletonBean(beanName);  
    if (sharedInstance != null){  
        return getObjectForBeanInstance(sharedInstance,beanName);  
    }  
  
    // 如果没有尝试创建Bean,Bean的创建需要通过BeanDefinition  
    BeanDefinition beanDefinition = getBeanDefinition(beanName);  
  
    if (beanDefinition == null){  
        throw new BeansException("beanDefinition：【" + beanName + "】 为空");  
    }  
  
    // 创建Bean  
    Object bean = createBean(beanName, beanDefinition);  
  
    return getObjectForBeanInstance(bean,beanName);  
  
}  
/**  
 * 根据Bean实例获取应返回的对象实例  
 * 此方法主要用于处理FactoryBean的实例，以确保正确地获取对象  
 *  
 * @param beanInstance Bean实例，可能是FactoryBean实例  
 * @param beanName Bean的名称，用于标识Bean  
 * @return 返回的对象实例，可能是FactoryBean创建的对象，也可能是Bean实例本身  
 */  
public Object getObjectForBeanInstance(Object beanInstance,String beanName){  
    // 初始化对象为传入的Bean实例  
    Object object = beanInstance;  
    // 判断是否为FactoryBean实例  
    if (beanInstance instanceof FactoryBean){  
        // 强制转换为FactoryBean类型  
        FactoryBean factoryBean = (FactoryBean) beanInstance;  
  
        try {  
            // 判断FactoryBean是否生成单例Bean  
            if (factoryBean.isSingleton()){  
                // 尝试从FactoryBean对象缓存中获取对象  
                object = factoryBeanObjectCache.get(beanName);  
                // 如果缓存中不存在，则调用FactoryBean的getObject方法创建对象，并存入缓存  
                if (object == null){  
                    object = factoryBean.getObject();  
                    this.factoryBeanObjectCache.put(beanName,object);  
                }  
            }else {  
                // 如果不是单例Bean，直接调用FactoryBean的getObject方法创建对象  
                object = factoryBean.getObject();  
            }  
        } catch (Exception e) {  
            // 如果FactoryBean在创建对象时抛出异常，重新包装并抛出BeansException  
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);  
        }  
    }  
  
    // 返回最终的对象实例  
    return object;  
}
```