package org.qlspringframework.test.ioc;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.supper.GenericConversionService;
import org.qlspringframework.core.convert.supper.StringToNumberConverterFactory;
import org.qlspringframework.test.common.StringToBooleanConverter;
import org.qlspringframework.test.common.StringToIntegerConverter;

/**
 * @author jixu
 * @title TypeConversionFirstPartTest
 * @date 2025/6/3 16:29
 */
public class TypeConversionFirstPartTest {
    @Test
    public void testStringToIntegerConverter(){
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer integer = converter.convert("10");
        Assert.assertEquals(Integer.valueOf(10),integer);
    }

    @Test
    public void testStringToNumberConverterFactory(){
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();
        Converter<String, Integer> converter = converterFactory.getConverter(Integer.class);
        Integer integer = converter.convert("10");
        Assert.assertEquals(Integer.valueOf(10),integer);
    }

    @Test
    public void testGenericConverter(){
        StringToBooleanConverter converter = new StringToBooleanConverter();
        Boolean aTrue =(Boolean)  converter.convert("true", String.class, Boolean.class);
        Assert.assertTrue(aTrue);
    }


}
