package org.qlspringframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean属性列表
 *
 * 该类用于管理Bean的属性集合，提供属性的添加和获取功能
 *
 * @author jixu
 * @title PropertyValues
 * @date 2025/4/6 23:00
 */
public class PropertyValues {

    // 定义一个列表用于保存Bean的PropertyValue
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 获取所有属性值数组
     *
     * @return PropertyValue[] 当前所有的属性值数组
     */
    public PropertyValue[] getPropertyValueList(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 添加一个属性值
     *
     * @param propertyValue 要添加的属性值对象
     */
    public void addPropertyValue(PropertyValue propertyValue){
        this.propertyValueList.add(propertyValue);
    }

    /**
     * 根据属性名称获取属性值
     *
     * 此方法遍历propertyValueList列表，查找与给定名称匹配的PropertyValue对象如果找到匹配的属性名，
     * 则返回对应的PropertyValue对象；如果没有找到，则返回null
     *
     * @param propertyName 要获取的属性名称
     * @return PropertyValue 匹配的属性值对象，如果不存在则返回null
     */
    public PropertyValue getPropertyValue(String propertyName){
        for (PropertyValue propertyValue : propertyValueList) {
            String name = propertyValue.getName();
            if (name.equals(propertyName)){
                return propertyValue;
            }
        }
        return null;
    }

}

