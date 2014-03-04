package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为512的字符
 * @author zoe
 */
public final class Char512TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char512";
    private final static int LENGTH = 512;

    @Override
    protected int getLength() {
        return Char512TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char512TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
