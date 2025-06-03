package org.qlspringframework.core.convert.converter;

import java.util.Objects;
import java.util.Set;

/**
 * @author jixu
 * @title GenericConverter
 * @date 2025/6/3 16:57
 */
public interface GenericConverter {

    // 执行类型转换逻辑
    Object convert(Object source, Class sourceType, Class targetType);

    // 获取到对应的ConvertiblePair配对关系对象
    Set<ConvertiblePair> getConvertibleTypes();


    /**
     * 用于管理“源类型”和“目标类型”的配对关系
     */
    public static final class ConvertiblePair{
        private final Class<?> sourceType;
        private final Class<?> targetType;


        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            ConvertiblePair that = (ConvertiblePair) object;
            return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType, targetType);
        }
    }
}
