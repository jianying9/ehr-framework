package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;

/**
 * DOUBLE有符号类型 [-1.7976931348623157×10+308, -4.94065645841246544×10-324]
 * @author zoe
 */
public final class DoubleSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be double signed";

    @Override
    protected String getErrorMessage() {
        return DoubleSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        boolean result = true;
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }
    
    @Override
    public String getRandomValue() {
        double value = NumberUtils.getRandomDoubleValue();
        String result = NumberUtils.numberDf.format(value);
        return result;
    }
}
