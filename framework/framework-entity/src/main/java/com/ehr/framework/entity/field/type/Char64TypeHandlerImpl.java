package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为64的字符
 * @author zoe
 */
public final class Char64TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char64";
    private final static int LENGTH = 64;

    @Override
    protected int getLength() {
        return Char64TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char64TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
