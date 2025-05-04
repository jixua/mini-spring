package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.DisposableBean;
import org.qlspringframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的单例Bean注册表实现类
 *
 * @author: jixu
 * @create: 2025-03-28 15:41
 **/
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 保存单列Bean的地方
    private Map<String , Object> singletonObjects = new ConcurrentHashMap<>();

    // 保存含有销毁方法的Bean的地方
    private Map<String , DisposableBean> disposableBeans = new ConcurrentHashMap<>();



    public void registerDisposableBean(String beanName , DisposableBean disposableBean){
        this.disposableBeans.put(beanName,disposableBean);
    }



    public void destroySingletons(){
        Set<String> beanNames = disposableBeans.keySet();
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            // 执行销毁方法
            disposableBean.destroy();
        }

    }



    /**
     * 获取单列Bean
     *
     * @param beanName Bean名称
     * @return Bean对象
     */
    @Override
    public Object getSingletonBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    /**
     * 添加单列Bean
     *
     * @param baenName Bean名称
     * @param bean Bean对象
     */
    @Override
    public void addSingletonBean(String baenName , Object bean) {
        this.singletonObjects.put(baenName , bean);
    }
}
