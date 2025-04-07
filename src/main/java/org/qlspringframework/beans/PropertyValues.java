package org.qlspringframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean属性列表
 *
 * @author jixu
 * @title PropertyValues
 * @date 2025/4/6 23:00
 */
public class PropertyValues {

    // 定义一个列表用于保存Bean的PropertyValue
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValue[] getPropertyValueList(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public void addPropertyValue(PropertyValue propertyValue){
        this.propertyValueList.add(propertyValue);
    }

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
