package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 三字节有符号类型-32768,32767
 * @author zoe
 */
public final class SmallIntSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be small int signed";
    private final static Pattern SMALL_INT_SIGNED_PATTERN = Pattern.compile("\\d|-[1-9]|[1-9]\\d{1,3}|[1-2]\\d{4}|-[1-9]\\d{1,3}|-[1-2]\\d{4}|-32768|-?(?:3[0-1]\\d{3}|32[0-6]\\d{2}|327[0-5]\\d|3276[0-7])");

    @Override
    protected String getErrorMessage() {
        return SmallIntSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        Matcher matcher = SmallIntSignedTypeHandlerImpl.SMALL_INT_SIGNED_PATTERN.matcher(value);
        return matcher.matches();
    }
    
    @Override
    public String getRandomValue() {
        int value = NumberUtils.getRandomIntegerValue(32767);
        return Integer.toString(value);
    }
}
