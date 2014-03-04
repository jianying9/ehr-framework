package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为256的字符
 * @author zoe
 */
public final class Char256TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char256";
    private final static int LENGTH = 256;

    @Override
    protected int getLength() {
        return Char256TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char256TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
