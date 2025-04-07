package org.qlspringframework.beans;

/**
 * Bean属性
 *
 * @author jixu
 * @title PropertyValue
 * @date 2025/4/6 22:59
 */
public class PropertyValue {

    private final String name;

    private final Object value;



    public PropertyValue(String name, String value) {
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
