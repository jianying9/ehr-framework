package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一字节带符号类型-128-127
 * @author zoe
 */
public final class TinyIntSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be tiny int signed";
    private final static Pattern TINY_INT_SIGNED_PATTERN = Pattern.compile("\\d|-[1-9]|[1-9]\\d|1[0-1]\\d|12[0-7]|-[1-9]\\d|-1[0-1]\\d|-12[0-7]|-128");

    @Override
    protected String getErrorMessage() {
        return TinyIntSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        Matcher matcher = TinyIntSignedTypeHandlerImpl.TINY_INT_SIGNED_PATTERN.matcher(value);
        return matcher.matches();
    }
    
    @Override
    public String getRandomValue() {
        int value = NumberUtils.getRandomIntegerValue(127);
        return Integer.toString(value);
    }
}
