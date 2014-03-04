package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.StringUtils;

/**
 * 长度为36的字符
 * @author zoe
 */
public final class Char36TypeHandlerImpl extends AbstractCharTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be char36";
    private final static int LENGTH = 36;

    @Override
    protected int getLength() {
        return Char36TypeHandlerImpl.LENGTH;
    }

    @Override
    protected String getErrorMessage() {
        return Char36TypeHandlerImpl.ERROR_MESSAGE;
    }
    
    @Override
    public String getRandomValue() {
        return StringUtils.getRandomStringValue(LENGTH);
    }
}
