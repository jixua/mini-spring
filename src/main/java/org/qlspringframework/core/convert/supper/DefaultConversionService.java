package org.qlspringframework.core.convert.supper;

import org.qlspringframework.core.convert.converter.ConverterRegister;
import org.qlspringframework.stereotype.Component;

/**
 * @author jixu
 * @title DefaultConversionService
 * @date 2025/6/5 18:11
 */
@Component("conversionService")
public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverters(this);

    }

    private void addDefaultConverters(ConverterRegister converterRegister) {
        converterRegister.addConverterFactory(new StringToNumberConverterFactory());
    }


}
