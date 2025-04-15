
## 一，实现一个简单的IOC容器

说起Spring其核心也就是在IOC容器当中，那么什么是IOC容器，简单来说就是用来保存已创建好的Bean的地方，那么在这个过程当中就又会涉及到Bean的创建，Bean的保存等等，那么现在我们就来尝试实现一个最简单的容器。

```java
public class BeanFactory {
	private Map<String, Object> beanMap = new HashMap<>();

	public void registerBean(String name, Object bean) {
		beanMap.put(name, bean);
	}

	public Object getBean(String name) {
		return beanMap.get(name);
	}
}
```

再来测试一下

```java
@Test
	public void testGetBean() throws Exception {
		BeanFactory beanFactory = new BeanFactory();
		beanFactory.registerBean("helloService", new HelloService());
		HelloService helloService = (HelloService) beanFactory.getBean("helloService");
		assertThat(helloService).isNotNull();
		assertThat(helloService.sayHello()).isEqualTo("hello");
	}

	class HelloService {
		public String sayHello() {
			System.out.println("hello");
			return "hello";
		}
	}
```

这样一个简单的容器就实现了，当然Spring不可能通过如此简单的逻辑实现的IOC容器，在Spring当中还对其进行了拓展包括`BeanDefinition`与`SingletonBeanRegistry`，这也是实现IOC容器的核心。

## 二，SingletonBeanRegistry

顾名思义`SingletonBeanRegistry`就是单列Bean的一个注册表，这是一个接口，提供单列Bean的获取与添加方法

```java
public interface SingletonBeanRegistry {  
  
    /**  
     * 获取单列Bean  
     * @param beanName Bean名称  
     * @return Bean对象  
     */  
    Object getSingletonBean(String beanName);  
  
    /**  
     * 添加单列Bean  
     *     * @param beanName Bean名称  
     * @param bean Bean对象  
     */  
    void addSingletonBean(String beanName, Object bean);  
}
```

既然是接口，那我我们就可以提供一个默认的实现类来实现它

```java
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {  
  
  
    // 保存单列Bean的地方  
    private Map<String , Object> singletonObjects = new ConcurrentHashMap<>();  
  
  
    /**  
     * 获取单列Bean  
     *     * @param beanName Bean名称  
     * @return Bean对象  
     */  
    @Override  
    public Object getSingletonBean(String beanName) {  
        return singletonObjects.get(beanName);  
    }  
  
    /**  
     * 添加单列Bean  
     *     * @param bean     */    @Override  
    public void addSingletonBean(String baenName , Object bean) {  
        this.singletonObjects.put(baenName , bean);  
    }  
}
```

那么之后`DefaultSingletonBeanRegistry`作为`SingletonBeanRegistry`的默认实现，就可以通过集成`DefaultSingletonBeanRegistry`来实现单列Bean的注册与获取。

在不考虑BeanDefinition的前提下，我们可以思考一下此时如何创建与获取Bean

## 三，通过SingletonBeanRegistry获取Bean

首先我们还是需要一个`BeanFactory`，但是现在需要的接口而不是类，这样便于我们的后续扩展

```java
public interface BeanFactory {  
  
    /**  
     * 获取Bean  
     * @param name Bean名称  
     */  
    public Object getBean(String name);  
  
}
```

之后我们再定义一个AbstractBeanFactory实现BeanFactory的getBean方法
这样我们就引入了SingletonBeanRegistry实现IOC容器，至于为什么要用SingletonBeanRegistry，这涉及到循环依赖的问题，我们以后再讲

```java
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry  implements BeanFactory {  
  
    
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
        // TODO: 如果缓存当中，没有就需要走Bean的创建逻辑，同时将创建好的Bean加入到缓存当中
    }  
  
}
```