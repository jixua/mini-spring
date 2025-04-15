# Mini-Spring Framework

## 项目简介
Mini-Spring 是一个简化版的 Spring 框架实现，旨在帮助开发者更好地理解 Spring 框架的核心原理和实现机制。该项目保留了 Spring 的核心功能，同时去除了复杂的细节，使得代码更加清晰易懂。

## 主要特性
- 基于 Java 8 开发
- 实现 IoC（控制反转）容器
- 支持依赖注入（DI）
- 包含基础的 AOP（面向切面编程）功能
- 简化的 Bean 生命周期管理

## 环境要求
- JDK 8 或更高版本
- Maven 3.6.x 或更高版本
- IDE 推荐：IntelliJ IDEA

## 技术栈
- 核心框架：自研 Mini-Spring 框架
- 构建工具：Maven
- 测试框架：JUnit 4.11
- 断言库：AssertJ 3.11.1
- 工具库：Hutool 5.5.0

## 快速开始
1. 克隆项目到本地
```bash
git clone [项目地址]
```

2. 使用 Maven 安装依赖
```bash
mvn clean install
```

3. 在 IDE 中导入项目
- 选择 "Import Project"
- 选择项目根目录
- 选择 Maven 项目类型
- 等待依赖下载完成

## 项目结构
```
src
├── main/java
│   └── org
│       └── qlspringframework
│           ├── beans        # Bean 相关实现
│           │   ├── factory  # Bean 工厂实现
│           │   └── support  # Bean 支持类
│           └── core        # 核心功能实现
└── test/java
    └── org
        └── qlspringframework
            └── beans        # Bean 相关测试
                └── factory  # Bean 工厂测试
```

## 使用示例
```java
// TODO: 添加具体的使用示例代码
```

## 开发计划
- [ ] 完善 IoC 容器
- [ ] 增强 AOP 功能
- [ ] 添加更多单元测试
- [ ] 支持注解配置
- [ ] 实现简单的 MVC 功能

## 参与贡献
1. Fork 本仓库
2. 创建新的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

## 开源协议
该项目使用 MIT 协议 - 查看 [LICENSE](LICENSE) 文件了解详情 