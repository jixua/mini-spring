

## 一，单例 Bean 注册的局限性

在前面的介绍中说道了通过 SingletonBeanRegistry 实现 getBean，但是效果比较单一只能从单例池中获取，如果单例池当中不存在就没法实现了。

所以在这里又引入 BeanDefinition，通过 Bean 的定义信息实现 Bean 的创建逻辑

创建一个简单的 BeanDefinition

```java
public class BeanDefinition {  
    // Bean名称  
    private String beanName;  
    // Bean的Class对象  
    private Class beanClass;  

    public BeanDefinition(Class beanClass){  
        this(beanClass,null );  
    }  

    public Class getBeanClass() {  
        return beanClass;  
    }  

    public void setBeanClass(Class beanClass) {  
        this.beanClass = beanClass;  
    }  

    public String getBeanName() {  
        return beanName;  
    }  

    public void setBeanName(String beanName) {  
        this.beanName = beanName;  
    }  
}
```

## 二，扩展AbstractBeanFactory实现完整获取逻辑

之后我们就可以对 `AbstractBeanFactory` 进行扩展，在单例池当中不存在的时候创建 Bean，那么由于创建 Bean 的逻辑需要获取到 BeanDefinition，那么 AbstractBeanFactory 就应该有 getBeanDefinition 的方法来获取到 BeanDefinition。

```java
@Override  
public Object getBean(String beanName) {  
    // 尝试从缓存当中获取Bean  
    Object bean = super.getSingletonBean(beanName);  
    if (bean != null){  
        return bean;  
    }  

    // 如果没有尝试创建Bean,Bean的创建需要通过BeanDefinition  
    BeanDefinition beanDefinition = getBeanDefinition(beanName);  

    // 创建Bean  
    return createBean(beanName , beanDefinition);  
}  

protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);  
protected abstract BeanDefinition getBeanDefinition(String beanName);
```


那么除此之外对于每一个 Bean 都需要拥有自己的 BeanDefinition，也就是说我们还需要一个类，类似于刚才的 AbstractSingletonBeanRegister，在其中定义保存不同类的 beanDefinition 的数据结构，并提供 get/set 方法。

```java
public interface BeanDefinitionRegister {  
    /**  
     * 注册Bean  
     * @param beanName  
     * @param beanDefinition     
     */    
    void registerBeanDefinition(String beanName , BeanDefinition beanDefinition);  
}
```


通过 `DefaultListableBeanFactory` 实现 BeanDefinitionRegister

```java
public class DefaultListableBeanFactory implements BeanDefinitionRegister {  
    private Map<String , BeanDefinition> beanDefinitionMap = new HashMap<>();  

    /**  
     * 注册BeanDefinition  
     * @param beanName  
     * @param beanDefinition  
     */    
    @Override  
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {  
        beanDefinitionMap.put(beanName,beanDefinition);  
    }  

    /**  
     * 获取BeanDefinition  
     * @param beanName  
     * @return  
     */    
    @Override  
    protected BeanDefinition getBeanDefinition(String beanName) {  
        return beanDefinitionMap.get(beanName);  
    }  
}
```

## 三，整体设计思路梳理

好了，现在我们再来捋一下思路，我们有了 SingletonBeanRegister 与其默认实现 DefaultSingletonBeanRegistry。通过单列池我们就可以获取加入到缓存当中的 Bean，但是在获取 bean 的过程当中如果 Bean 不存在于单列池当中，我们就需要去创建 Bean，再讲创建好的 Bean 放入到单列池当中。除此之外在创建 Bean 的过程当中又会涉及到 BeanDefinition，通过 BeanDefinition 当中的 Class 属性反射创建对象，所以我们又创建了 BeanDefinition 与 DefaultSingletonBeanRegistry。


那么流程讲完了我们重新回到 `AbstractBeanFactory` 补充创建 Bean 与获取 BeanDefinition 的逻辑

在这里我们由于获取 BeanDefinition 在单一的 AbstractBeanFactory 是无法实现的所以包含创建 Bean 的方法在内我们都委托子类来实现

```java
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {  
    /**  
     * 获取Bean  
     * 包含创建Bean的流程，在创建Bean的流程当中会先从缓存当中取，如果没有则创建  
     * 在获取Bean之前需要获取到Bean的定义信息也就是BeanDefinition  
     * 1，从缓存当中获取Bean  
     * 2，尝试创建Bean并返回  
     *  
     * @param beanName Bean名称  
     */  
    @Override  
    public Object getBean(String beanName) {  
        // 尝试从缓存当中获取Bean  
        Object bean = super.getSingletonBean(beanName);  
        if (bean != null){  
            return bean;  
        }  

        // 如果没有尝试创建Bean,Bean的创建需要通过BeanDefinition  
        BeanDefinition beanDefinition = getBeanDefinition(beanName);  

        // 创建Bean  
        return createBean(beanName , beanDefinition);  
    }  

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);  
    protected abstract BeanDefinition getBeanDefinition(String beanName);  
}
```


`AbstractAutowireCapableBeanFactory` 实现 `createBean` 方法

```java
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {  
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();  

    /**  
     * @param beanName Bean名称  
     * @param beanDefinition Bean的定义信息  
     * @return Bean实列  
     */  
    @Override  
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {  
        return doCreateBean(beanName , beanDefinition);  
    }  

    /**  
     * 执行具体创建Bean的逻辑  
     *  
     * 如何创建Bean？  
     * 通过beanDefinition当中保存的Bean的Class对象，通过反射的方式创建Bean  
     */    
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeanException {  
        // 通过反射创建对象  
        Object bean = null;  
        try {  
            // 通过InstantiationStrategy实例化Bean  
            bean = createBeanInstance(beanDefinition);  
        } catch (Exception e) {  
            throw new BeanException(e.getMessage());  
        }  

        // 创建完毕后加入缓存  
        super.addSingletonBean(beanName, bean);  

        return bean;  
    }  

    /**  
     * 创建并返回一个Bean实例  
     * 此方法根据Bean定义来实例化Bean，具体实例化策略由获取到的实例化策略决定  
     *  
     * @param beanDefinition Bean的定义，包含了创建Bean实例所需的信息  
     * @return 实例化的Bean对象  
     */  
    private Object createBeanInstance(BeanDefinition beanDefinition) { 
        // 获取BeanDefinition中定义的类  
        Class aClass = beanDefinition.getBeanClass();  
        try {  
            // 获取无参构造  
            Constructor declaredConstructor = aClass.getDeclaredConstructor();  
            // 使用无参构造实例化对象并返回  
            return declaredConstructor.newInstance();  
        } catch (Exception e) {  
            // 如果实例化过程中发生异常，抛出BeanException  
            throw new BeanException(e.getMessage());  
        } 
    }  
}
```

## 四，最终容器类设计

这里我们来想一下，getBeanDefinition 这个方法是在 `DefaultListableBeanFactory` 当中实现的，但是我们的 AbstractAutowireCapableBeanFactory 需要用到这个方法，而且在它的父类 AbstractBeanFactory 当中其实也定义了该抽象方法，在这里我们其实也可以让 AbstractAutowireCapableBeanFactory 实现 BeanDefinitionRegistry 然后与 DefaultListableBeanFactory 一样实现相关的方法与逻辑，但是这样做的话会导致太臃肿了。所以在这里我们可以让 DefaultListableBeanFactory 继承 AbstractAutowireCapableBeanFactory，这样他就包含了创建 Bean 的功能，同时也实现了 BeanDefinition 的注册与获取

我们再看一下继承关系

```java
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegister {  
}
```

从这张图就可以看出所有的继承关系了，我们也可以发现 DefaultListableBeanFactory 同时包含了 SingletonBeanRegister 与 BeanDefinitionRegistry 的所有功能，也是容器的核心类

![](../assets/Pasted%20image%2020250415165939.png)


## 五，如何在Bean中注入Bean


在Bean实例化之后需要对其中的属性进行注入，如下我们定义了一个People，现在需要将其加入到Spring容器当中，其中还包含了两个属性`name`,`age`

```java
public class People {  
  
    private String name;  
    private Integer age;  
  
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
                '}';  
    }  
}
```

最终我们需要实现在BeanDefinition当中注入属性，如下

```java
/**  
 * 测试Bean属性引用。  
 * 该测试方法通过DefaultListableBeanFactory注册两个Bean定义：Car和People。  
 * People Bean的car属性引用Car Bean，验证属性引用是否正确。  
 */  
@Test  
public void testBeanPropertyReference() {  
    // 创建Bean工厂  
    DefaultListableBeanFactory factory = new DefaultListableBeanFactory();  
  
    // 创建Car Bean的属性值集合，并添加name属性  
    PropertyValues carValues = new PropertyValues();  
    carValues.addPropertyValue(new PropertyValue("name", "Xiaomi"));  
  
    // 创建Car类的Bean定义，并注册到工厂中  
    BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, carValues);  
    factory.registerBeanDefinition("car", carBeanDefinition);  
  
    // 创建People Bean的属性值集合，并添加age、name和car属性（car属性引用Car Bean）  
    PropertyValues propertyValues = new PropertyValues();  
    propertyValues.addPropertyValue(new PropertyValue("age", 20));  
    propertyValues.addPropertyValue(new PropertyValue("name", "jixu"));  
    propertyValues.addPropertyValue(new PropertyValue("car", new BeanReference("car")));  
  
    // 创建People类的Bean定义，并注册到工厂中  
    BeanDefinition peopleBeanDefinition = new BeanDefinition(People.class, propertyValues);  
    factory.registerBeanDefinition("people", peopleBeanDefinition);  
  
    // 从工厂中获取People Bean，并验证其属性值  
    People people = (People) factory.getBean("people");  
    System.out.println(people.toString());  
    Assertions.assertThat(people.getAge()).isEqualTo(20);  
    Assertions.assertThat(people.getName()).isEqualTo("jixu");  
}
```


首先我们需要创建`PropertyValue`类作为Bean属性的存储对象，那么对于一个Bean来说可能有多个属性，单靠一个`PropertyValue`无法实现，我们需要一个列表来储存`Property`。所以我们还需要抽取一个`PropertyValues`，同时还可以在该类当中定义一些工具方法。

```java
public class PropertyValue {  
  
    private final String name;  
  
    private final Object value;  
  
  
  
    public PropertyValue(String name, Object value) {  
        this.name = name;  
        this.value = value;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public Object getValue() {  
        return value;  
    }  
}
```

```java
/**
 * Bean属性列表
 *
 * 该类用于管理Bean的属性集合，提供属性的添加和获取功能
 *
 * @author jixu
 * @title PropertyValues
 * @date 2025/4/6 23:00
 */
public class PropertyValues {

    // 定义一个列表用于保存Bean的PropertyValue
    private final List<PropertyValue> propertyValueList = new ArrayList<>();
    
    /**
     * 获取所有属性值数组
     *
     * @return PropertyValue[] 当前所有的属性值数组
     */
    public PropertyValue[] getPropertyValueList(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 添加一个属性值
     *
     * @param propertyValue 要添加的属性值对象
     */
    public void addPropertyValue(PropertyValue propertyValue){
        this.propertyValueList.add(propertyValue);
    }

    /**
     * 根据属性名称获取属性值
     *
     * 此方法遍历propertyValueList列表，查找与给定名称匹配的PropertyValue对象如果找到匹配的属性名，
     * 则返回对应的PropertyValue对象；如果没有找到，则返回null
     *
     * @param propertyName 要获取的属性名称
     * @return PropertyValue 匹配的属性值对象，如果不存在则返回null
     */
    public PropertyValue getPropertyValue(String propertyName){
        for (PropertyValue propertyValue : propertyValueList) {
            String name = propertyValue.getName();
            if (name.equals(propertyName)){
                return propertyValue;
            }
        }
        return null;
    }

}

```

之后我们只需要修改`AbstractAutowireCapableBeanFactory`当中的`doCreate`方法

```java
// 为Bean的属性进行赋值  
applyPropertyValues(bean , beanDefinition , name);
```

```java
/**  
 * 根据BeanDefinition中的属性信息，为指定的bean对象应用属性值  
 * 此方法主要用于依赖注入过程，通过反射机制将属性值注入到bean实例中  
 *  
 * @param bean 要应用属性值的目标bean对象  
 * @param beanDefinition 包含bean定义和属性信息的对象  
 * @param beanName bean的名称，用于错误信息或日志记录中  
 */  
private void applyPropertyValues(Object bean, BeanDefinition beanDefinition, String beanName) {  
    try {  
        // 获取到要操作Bean到Class对象  
        Class beanClass = beanDefinition.getBeanClass();  
  
        // 循环获取当前Bean的所有属性  
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {  
            // 对于属性的赋值要通过对应的set方法，构造出set方法的方法名  
            String name = propertyValue.getName();  
            String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);  
  
            //通过属性的set方法设置属性  
            Class<?> type = beanClass.getDeclaredField(name).getType();  
  
            // 通过反射动态调用  
            Method declaredMethod = beanClass.getDeclaredMethod(setMethodName, type);  
            declaredMethod.invoke(bean,propertyValue.getValue());  
        }  
    }catch (Exception e){  
        throw new BeanException(String.format("bean 属性注入异常[%s]",beanName) ,  e);  
    }  
}
```