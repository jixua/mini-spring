package org.qlspringframework.beans.factory.xml;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.qlspringframework.beans.BeansException;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanReference;
import org.qlspringframework.beans.factory.supper.AbstractBeanDefinitionReader;
import org.qlspringframework.beans.factory.supper.BeanDefinitionRegister;
import org.qlspringframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.qlspringframework.core.io.Resource;
import org.qlspringframework.core.io.ResourceLoader;


import java.io.InputStream;
import java.util.List;



/**
 * XMLBean定义读取器
 * 该类负责从XML配置文件中读取Bean定义，并注册到BeanDefinitionRegister中
 *
 * @author: jixu
 * @create: 2025-04-10 14:31
 **/
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    // 定义XML中Bean标签的元素名称
    public static final String BEAN_ELEMENT = "bean";
    // 定义XML中属性标签的元素名称
    public static final String PROPERTY_ELEMENT = "property";
    // 定义XML中ID属性的名称
    public static final String ID_ATTRIBUTE = "id";
    // 定义XML中名称属性的名称
    public static final String NAME_ATTRIBUTE = "name";
    // 定义XML中类属性的名称
    public static final String CLASS_ATTRIBUTE = "class";
    // 定义XML中值属性的名称
    public static final String VALUE_ATTRIBUTE = "value";
    // 定义XML中引用属性的名称
    public static final String REF_ATTRIBUTE = "ref";

    public static final String SCOPE_ATTRIBUTE = "scope";

    private static final String INIT_METHOD_ATTRIBUTE = "init-method";

    private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";

    /**
     * 构造函数
     * beanDefinitionRegister是用来注册BeanDefinition使用的
     * 其子类DefaultListableBeanFactory实现了beanDefinitionRegister与BeanFactory
     * 可以通过DefaultListableBeanFactory获取、创建Bean
     *
     * @param beanDefinitionRegister 用于注册BeanDefinition的实例
     */
    public XmlBeanDefinitionReader(BeanDefinitionRegister beanDefinitionRegister) {
        super(beanDefinitionRegister);
    }

    /**
     * 根据指定的资源位置加载BeanDefinition。
     *
     * @param location 资源的位置，通常为文件路径或URL
     */
    @Override
    public void loadBeanDefinitions(String location) {
        // 通过ResourceLoad获取到Resource
        ResourceLoader resourceLoad = getResourceLoad();
        Resource resource = resourceLoad.getResource(location);
        this.loadBeanDefinitions(resource);
    }

    /**
     * 根据指定的Resource对象加载BeanDefinition。
     *
     * @param resource 资源对象，包含具体的资源信息
     */
    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实际加载Bean定义的方法
     * 该方法解析XML文档，提取Bean定义信息，并将它们注册到BeanDefinitionRegister
     *
     * @param inputStream XML资源的输入流
     * @throws Exception 解析XML或注册Bean定义时可能抛出的异常
     */
    protected void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);

        Element root = document.getRootElement();

        //解析context:component-scan标签并扫描指定包中的类，提取类信息，组装成BeanDefinition
        Element componentScan = root.element(COMPONENT_SCAN_ELEMENT);
        if (componentScan != null) {
            String scanPath = componentScan.attributeValue(BASE_PACKAGE_ATTRIBUTE);
            if (StrUtil.isEmpty(scanPath)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        List<Element> beanList = root.elements(BEAN_ELEMENT);
        for (Element bean : beanList) {
            String beanId = bean.attributeValue(ID_ATTRIBUTE);
            String beanName = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);


            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException("没有找到类 [" + className + "]");
            }
            //id优先于name
            beanName = StrUtil.isNotEmpty(beanId) ? beanId : beanName;
            if (StrUtil.isEmpty(beanName)) {
                //如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements(PROPERTY_ELEMENT);
            for (Element property : propertyList) {
                String propertyNameAttribute = property.attributeValue(NAME_ATTRIBUTE);
                String propertyValueAttribute = property.attributeValue(VALUE_ATTRIBUTE);
                String propertyRefAttribute = property.attributeValue(REF_ATTRIBUTE);

                if (StrUtil.isEmpty(propertyNameAttribute)) {
                    throw new BeansException("name 属性不能为 null 或空");
                }

                Object value = propertyValueAttribute;
                if (StrUtil.isNotEmpty(propertyRefAttribute)) {
                    value = new BeanReference(propertyRefAttribute);
                }
                PropertyValue propertyValue = new PropertyValue(propertyNameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                //beanName不能重名
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }


    }

    /**
     * 扫描注解Component的类，提取信息，组装成BeanDefinition
     *
     * @param scanPath
     */
    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
