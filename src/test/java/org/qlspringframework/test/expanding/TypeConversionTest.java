package org.qlspringframework.test.expanding;

import org.junit.Assert;
import org.junit.Test;
import org.qlspringframework.core.convert.converter.Converter;
import org.qlspringframework.core.convert.supper.GenericConversionService;
import org.qlspringframework.core.convert.supper.StringToNumberConverterFactory;
import org.qlspringframework.test.common.StringToBooleanConverter;
import org.qlspringframework.test.common.StringToIntegerConverter;

/**
 * @author jixu
 * @title TypeConversionTest
 * @date 2025/6/3 16:29
 */
public class TypeConversionTest {
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

    @Test
    public void testGenericConversionService(){
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        Integer integer = conversionService.convert("10", Integer.class);
        Assert.assertEquals(Integer.valueOf(10),integer);

        conversionService.addConverterFactory(new StringToNumberConverterFactory());
        Long aLong = conversionService.convert("10", Long.class);
        Assert.assertEquals(Long.valueOf(10),aLong);


    }
}
