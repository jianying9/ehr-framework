package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为8的字符
 * @author zoe
 */
public final class Char8TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char8";

    private final static int LENGTH = 8;

    @Override
    protected int getLength() {
        return LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
