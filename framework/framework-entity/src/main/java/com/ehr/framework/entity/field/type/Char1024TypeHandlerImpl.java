package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为1024的字符
 * @author zoe
 */
public final class Char1024TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char1024";
    private final static int LENGTH = 1024;

    @Override
    protected int getLength() {
        return Char1024TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char1024TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
