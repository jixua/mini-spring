package org.qlspringframework.context;

import org.qlspringframework.beans.factory.HierarchicalBeanFactory;
import org.qlspringframework.beans.factory.ListableBeanFactory;
import org.qlspringframework.core.io.ResourceLoader;

/**
 * ApplicationContext 接口是 Spring 框架中的核心接口之一，用于提供应用程序的配置信息。
 * 它继承了 ListableBeanFactory、HierarchicalBeanFactory 和 ResourceLoader 接口，
 * 从而具备了以下功能：
 * 1. 列出所有 Bean 定义的能力（通过 ListableBeanFactory）。
 * 2. 支持 Bean 工厂的层次结构（通过 HierarchicalBeanFactory）。
 * 3. 加载资源文件的能力（通过 ResourceLoader）。
 *
 * 该接口通常用于在 Spring 应用程序中获取 Bean 实例、管理 Bean 的生命周期以及加载资源。
 */
public interface ApplicationContext extends ListableBeanFactory , HierarchicalBeanFactory , ResourceLoader {

}
