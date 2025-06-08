package org.qlspringframework.beans.factory.supper;

import org.qlspringframework.beans.factory.DisposableBean;
import org.qlspringframework.beans.factory.ObjectFactory;
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

    // 一级缓存，保存单列Bean的地方
    private Map<String , Object> singletonObjects = new ConcurrentHashMap<>(255);

    // 二级缓存，保存实例化后的Bean
    protected Map<String , Object> earlySingletonObjects = new ConcurrentHashMap<>(255);

    // 三级缓存
    protected Map<String , ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>(16);


    // 保存含有销毁方法的Bean的地方
    private Map<String , DisposableBean> disposableBeans = new ConcurrentHashMap<>();



    public void registerDisposableBean(String beanName , DisposableBean disposableBean){
        this.disposableBeans.put(beanName,disposableBean);
    }

    public void addSingletonFactory(String beanName , ObjectFactory<?> objectFactory){
        singletonFactories.put(beanName,objectFactory);
    }

    public void destroySingletons(){
        Set<String> beanNames = disposableBeans.keySet();
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            // 执行销毁方法
            disposableBean.destroy();
            // 删除缓存当中的
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
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null){
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null){
                ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                if (objectFactory != null){
                    // 获取到真实的对象引用
                    singletonObject = objectFactory.getObject();

                    // 添加到二级缓存当中
                    earlySingletonObjects.put(beanName,singletonObject);

                    // 三级缓存移除当前Bean
                    singletonFactories.remove(beanName);
                }

            }
        }
        return singletonObject;
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
