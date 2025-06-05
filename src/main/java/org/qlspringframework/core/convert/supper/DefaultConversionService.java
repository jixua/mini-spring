package org.qlspringframework.core.convert.supper;

import org.qlspringframework.core.convert.converter.ConverterRegister;

/**
 * @author jixu
 * @title DefaultConversionService
 * @date 2025/6/5 18:11
 */
public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverters(this);

    }

    private void addDefaultConverters(ConverterRegister converterRegister) {
        converterRegister.addConverterFactory(new StringToNumberConverterFactory());
    }
}
