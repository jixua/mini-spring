package org.qlspringframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.qlspringframework.beans.PropertyValue;
import org.qlspringframework.beans.factory.config.BeanDefinition;
import org.qlspringframework.beans.factory.config.BeanReference;
import org.qlspringframework.beans.factory.supper.AbstractBeanDefinitionReader;
import org.qlspringframework.beans.factory.supper.BeanDefinitionRegister;
import org.qlspringframework.core.io.Resource;
import org.qlspringframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;

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

    private static final String INIT_METHOD_ATTRIBUTE = "init-method";

    private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

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
    protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
                    //解析bean标签
                    Element bean = (Element) childNodes.item(i);
                    String id = bean.getAttribute(ID_ATTRIBUTE);
                    String name = bean.getAttribute(NAME_ATTRIBUTE);
                    String className = bean.getAttribute(CLASS_ATTRIBUTE);
                    String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
                    String destroyMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);

                    Class<?> clazz = Class.forName(className);
                    //id优先于name
                    String beanName = StrUtil.isNotEmpty(id) ? id : name;
                    if (StrUtil.isEmpty(beanName)) {
                        //如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                        beanName = StrUtil.lowerFirst(clazz.getSimpleName());
                    }

                    BeanDefinition beanDefinition = new BeanDefinition(clazz);
                    beanDefinition.setInitMethodName(initMethodName);
                    beanDefinition.setDestroyMethodName(destroyMethodName);

                    for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                        if (bean.getChildNodes().item(j) instanceof Element) {
                            if (PROPERTY_ELEMENT.equals(((Element) bean.getChildNodes().item(j)).getNodeName())) {
                                //解析property标签
                                Element property = (Element) bean.getChildNodes().item(j);
                                String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
                                String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
                                String refAttribute = property.getAttribute(REF_ATTRIBUTE);

                                Object value = valueAttribute;
                                if (StrUtil.isNotEmpty(refAttribute)) {
                                    value = new BeanReference(refAttribute);
                                }
                                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                            }
                        }
                    }
                    getRegistry().registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }
    }
}
