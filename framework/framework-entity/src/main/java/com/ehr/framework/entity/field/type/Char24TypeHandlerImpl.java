package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为24的字符
 * @author zoe
 */
public final class Char24TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char24";
    private final static int LENGTH = 24;

    @Override
    protected int getLength() {
        return Char24TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char24TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
