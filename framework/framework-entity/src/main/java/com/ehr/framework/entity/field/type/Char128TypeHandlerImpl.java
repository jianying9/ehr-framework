package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为128的字符
 * @author zoe
 */
public final class Char128TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char128";
    private final static int LENGTH = 128;

    @Override
    protected int getLength() {
        return Char128TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char128TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
