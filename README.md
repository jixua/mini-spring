# Mini-Spring Framework


## 项目简介

Mini-Spring  是一个简化版的 Spring 框架，抽取并还原了 Spring 的核心设计理念，保留 IoC 容器、AOP、资源加载器、事件监听器、类型转换、容器扩展点、Bean 生命周期与作用域、应用上下文等核心功能，帮助深入理解框架底层原理。

---

## IOC

- [1，如何实现一个简单的IOC容器](document/IOC/1，如何实现一个简单的IOC容器.md)
- [2，如何通过BeanDefinition定义Bean](document/IOC/2，如何通过BeanDefinition定义Bean.md)
- [3，如何实现资源文件的加载](document/IOC/3，如何实现资源文件的加载.md)
- [4，如何通过XML注册Bean](document/IOC/4，如何通过XML注册Bean.md)
- [5，PostProcess容器扩展机制](document/IOC/5，PostProcess容器扩展机制.md)
- [6，Application应用上下文](document/IOC/6，Application应用上下文.md)
- [7，Bean的初始化和销毁](document/IOC/7，Bean的初始化和销毁.md)
- [8，Aware接口](document/IOC/8，Aware接口.md)
- [9，实现scope作用域支持](document/IOC/9，实现scope作用域支持.md)
- [10，FactoryBean](document/IOC/10，FactoryBean.md)
- [11，ApplicationEvent事件监听器](document/IOC/9，实现scope作用域支持.md)
- [12，SpringIOC容器总结](document/IOC/12，SpringIOC容器总结.md)

## AOP

-  [AOP相关核心概念与作用说明（补充）](document/AOP/AOP相关核心概念与作用说明（补充）.md)
- [1，PointCut切入点](document/AOP/1，PointCut切入点.md)
- [2，AOP动态代理](document/AOP/2，AOP动态代理.md)
- [3，ProxyFactory代理工厂](document/AOP/3，ProxyFactory代理工厂类.md)
- [4，Advice横切关注点的逻辑实现](document/AOP/4，Advice横切关注点的逻辑实现.md)
- [5，Advisor增强规则](document/AOP/5，Advisor增强规则.md)
- [6，在Spring生命周期中织入代理逻辑](document/AOP/6，在Spring生命周期中织入代理逻辑.md)




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