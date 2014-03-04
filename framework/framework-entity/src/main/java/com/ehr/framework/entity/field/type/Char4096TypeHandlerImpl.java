package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为4096的字符
 * @author zoe
 */
public final class Char4096TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char4096";
    private final static int LENGTH = 4096;

    @Override
    protected int getLength() {
        return Char4096TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char4096TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
