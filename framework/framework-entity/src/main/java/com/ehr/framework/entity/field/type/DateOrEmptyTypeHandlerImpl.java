package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.TimeUtils;
import java.util.regex.Matcher;

/**
 * 时间类型YYYY-m-d,YYYY-MM-DD
 * @author zoe
 */
public final class DateOrEmptyTypeHandlerImpl extends AbstractDateTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be date or empty";

    @Override
    protected String getErrorMessage() {
        return DateOrEmptyTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        boolean result = false;
        if (value.isEmpty()) {
            result = true;
        } else {
            if (value.length() > 7 && value.length() < 11) {
                Matcher matcher = AbstractDateTypeHandler.DATA_PATTERN.matcher(value);
                result = matcher.matches();
            }
        }
        return result;
    }
    
    @Override
    public String getRandomValue() {
        return TimeUtils.getDateFotmatYYMMDD();
    }
}
