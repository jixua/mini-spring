package org.qlspringframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.PropertyValues;
import org.qlspringframework.beans.factory.BeanFactory;
import org.qlspringframework.beans.factory.BeanFactoryAware;
import org.qlspringframework.beans.factory.ConfigurableListableBeanFactory;
import org.qlspringframework.beans.factory.config.BeanReference;
import org.qlspringframework.beans.factory.config.ConfigurableBeanFactory;
import org.qlspringframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.qlspringframework.core.convert.ConversionService;
import org.qlspringframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author jixu
 * @title AutowiredAnnotationBeanPostProcessor
 * @date 2025/5/31 16:41
 */

@Component
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor , BeanFactoryAware {


    private ConfigurableListableBeanFactory beanFactory;


    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, Object bean, String beanName) {

        Class<?> beanClass = bean.getClass();
        PropertyValues pvs = new PropertyValues();

        // 获取到当前类当中声明的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {

            // 获取到标记Value注解的属性
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null){
                Object value = valueAnnotation.value();

                // 解析Value的属性值，判断是否需要替换占位符
                value = beanFactory.resolveEmbeddedValue((String) value);

                Class<?> sourceClass = value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getType(field);

                // 类型转换
                ConversionService conversionService = beanFactory.getConversionService();
                if (conversionService != null){
                    boolean canConvert = conversionService.canConvert(sourceClass, targetType);
                    if (canConvert){
                        value = conversionService.convert(value,targetType);
                    }

                }

                // 将解析完毕的字段添加到类属性当中
                // BeanUtil.setFieldValue(bean,field.getName(),value);
                pvs.addPropertyValue(new PropertyValue(field.getName(), value));

            }
        }


        // 解析Autowired
        for (Field field : declaredFields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null){
                Class<?> type = field.getType();

                String dependentBeanName = null;
                // 判断是否指定Bean名称
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                Object dependentBean = null;
                if (qualifier != null){
                    dependentBeanName = qualifier.value();
                    dependentBean = beanFactory.getBean(dependentBeanName);
                }else {
                    dependentBean = beanFactory.getBean(type);
                }

                // 在Spring原生的逻辑当中较为复杂，这里只通过反射直接字段注入注入
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
                // pvs.addPropertyValue(new PropertyValue(field.getName(), dependentBean));
            }


        }


        return pvs;
    }





    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    /**
     * 在 Bean 初始化之前执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 被初始化之前对其进行修改或执行其他操作。
     *
     * @param bean     当前正在初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    /**
     * 在 Bean 初始化之后执行自定义处理逻辑。
     * 使用此方法，可以在 Bean 初始化完成后对其进行进一步的修改或执行其他操作。
     *
     * @param bean     当前已经初始化的 Bean 实例。
     * @param beanName 当前 Bean 的名称。
     * @return 返回处理后的 Bean 实例，可以是原始 Bean 或修改后的 Bean。
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}
