package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为32的字符
 * @author zoe
 */
public final class Char32TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char32";
    private final static int LENGTH = 32;

    @Override
    protected int getLength() {
        return Char32TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char32TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
