package org.qlspringframework.beans;

/**
 * 主要用于在Bean定义中描述单个属性的名称和值。
 *
 * @author jixu
 * @title PropertyValue
 * @date 2025/4/6 22:59
 */
public class PropertyValue {

    private final String name;

    private final Object value;



    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
