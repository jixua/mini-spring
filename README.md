# Mini-Spring Framework


## 项目简介

Mini-Spring  是一个简化版的 Spring 框架，抽取并还原了 Spring 的核心设计理念，保留 IoC 容器、AOP、资源加载器、事件监听器、类型转换、容器扩展点、Bean 生命周期与作用域、应用上下文等核心功能，帮助深入理解框架底层原理。

---
## 核心模块

### IOC容器实现

- [1，IOC容器基础实现原理](document/IOC/1，IOC容器基础实现原理.md)
- [2，BeanDefinition的定义与元数据管理](document/IOC/2，BeanDefinition的定义与元数据管理.md)
- [3，资源文件的加载与解析机制](document/IOC/3，资源文件的加载与解析机制.md)
- [4，基于XML的Bean注册与配置](document/IOC/4，基于XML的Bean注册与配置.md)
- [5，容器扩展机制：PostProcessor详解](document/IOC/5，容器扩展机制：PostProcessor详解.md)
- [6，ApplicationContext的设计与实现](document/IOC/6，ApplicationContext的设计与实现.md)
- [7，Bean生命周期：初始化与销毁方法](document/IOC/7，Bean生命周期：初始化与销毁方法.md)
- [8，Aware接口与容器感知能力](document/IOC/8，Aware接口与容器感知能力.md)
- [9，Bean作用域（Scope）的实现](document/IOC/9，Bean作用域（Scope）的实现.md)
- [10，FactoryBean的特殊实例化逻辑](document/IOC/10，FactoryBean的特殊实例化逻辑.md)
- [11，ApplicationEvent事件监听器](document/IOC/9，Bean作用域（Scope）的实现.md)
- [12，SpringIOC容器总结](document/IOC/12，SpringIOC容器总结.md)

### AOP实现

-  [AOP相关核心概念与作用说明（补充）](document/AOP/AOP相关核心概念与作用说明（补充）.md)
- [1，切入点（PointCut）的实现原理](document/AOP/1，切入点（PointCut）的实现原理.md)
- [2，AOP动态代理](document/AOP/2，AOP动态代理.md)
- [3，ProxyFactory代理工厂](document/AOP/3，ProxyFactory代理工厂设计.md)
- [4，Advice通知类型的实现逻辑](document/AOP/4，Advice通知类型的实现逻辑.md)
- [5，Advisor增强规则与组合逻辑](document/AOP/5，Advisor增强规则与组合逻辑.md)
- [6，代理对象在生命周期中的织入时机](document/AOP/6，代理对象在生命周期中的织入时机.md)


### 拓展功能
#### 注解配置

- [1，属性占位符解析](document/扩展/1，属性占位符解析.md)
- [2，包扫描与包扫描与Component注解配置](document/扩展/2，组件扫描与@Component注解原理.md)
- [3，@Value注解的依赖注入实现](document/扩展/3，@Value注解的依赖注入实现.md)
- [4，@Autowired自动装配的实现机制](document/扩展/4，@Autowired自动装配的实现机制.md)

---

## 开发计划

- [x] 完善 IoC 容器，Bean生命周期管理
- [x] 实现事件机制
- [x] 实现容器扩展点
- [ ] 构建 AOP 框架
- [ ] 支持注解配置
- [ ] 类型转换
- [ ] 循环依赖


---

## 参考

- [《mini-spring》](https://github.com/DerekYRC/mini-spring)
- [《Spring源码深度解析》](https://book.douban.com/subject/25866350/)