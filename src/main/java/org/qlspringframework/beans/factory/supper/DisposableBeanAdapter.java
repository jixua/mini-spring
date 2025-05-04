package org.qlspringframework.beans.factory.supper;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.factory.DisposableBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jixu
 * @title DisposableBeanAdapter
 * @date 2025/5/3 14:50
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final String beanName;

    private final String destroyMethodName;


    public DisposableBeanAdapter(Object bean, String beanName, String destroyMethodName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = destroyMethodName;
    }


    @Override
    public void destroy() {

        /**
         * 有一下情况会调用销毁方法
         * 1，用户继承并实现DisposableBean的destroy方法
         * 2，用户通过XML指定，销毁方法（destroy-method="destroy"）
         *
         * 在执行的过程当中1的优先级是大于2的，其中还有一个极端情况就是用户不仅实现了destroy方法而且其自定义销毁方法的方法名与之同名
         */

        // 如果该Bean继承并实现了 DisposableBean的destroy方法，则执行该方法，销毁Bean
        if (bean instanceof DisposableBean){
            // 执行销毁方法
            ((DisposableBean) bean).destroy();
        }

        // 避免用户不仅实现了destroy方法而且其自定义销毁方法的方法名与之同名
        if (StrUtil.isNotEmpty(destroyMethodName) && ( !(bean instanceof DisposableBean) && StrUtil.equals("destroy",destroyMethodName))){
            // 通过反射获取到该方法
            Method method = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
            try {
                method.invoke(bean);
            } catch (Exception e) {
                throw new BeansException(String.format("在Bean：%s 当中找不到名为：%s 的销毁方法",beanName,destroyMethodName));
            }
        }


    }
}
