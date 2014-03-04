package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 三字节有符号类型-8388608,8388607
 * @author zoe
 */
public final class MediumIntSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be medium int signed";
    private final static Pattern MEDIUM_INT_SIGNED_PATTERN = Pattern.compile("\\d|-[1-9]|[1-9]\\d{1,5}|[1-7]\\d{6}|-[1-9]\\d{1,5}|-[1-7]\\d{6}|-8388608|-?(?:8[0-2]\\d{5}|83[0-7]\\d{4}|838[0-7]\\d{3}|8388[0-5]\\d{2}|838860[0-7])");

    @Override
    protected String getErrorMessage() {
        return MediumIntSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        Matcher matcher = MediumIntSignedTypeHandlerImpl.MEDIUM_INT_SIGNED_PATTERN.matcher(value);
        return matcher.matches();
    }
    
    @Override
    public String getRandomValue() {
        int value = NumberUtils.getRandomIntegerValue(8388607);
        return Integer.toString(value);
    }
}
