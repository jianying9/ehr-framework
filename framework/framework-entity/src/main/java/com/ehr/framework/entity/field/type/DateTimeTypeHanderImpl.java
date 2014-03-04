package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.TimeUtils;
import java.util.regex.Matcher;

/**
 * 时间类型YYYY-MM-DD HH:MM,YYYY-MM-DD HH:MM:SS
 * @author zoe
 */
public final class DateTimeTypeHanderImpl extends AbstractDateTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be datetime";

    @Override
    protected String getErrorMessage() {
        return DateTimeTypeHanderImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        boolean result = false;
        if (value.length() > 10 && value.length() < 20) {
            Matcher matcher = AbstractDateTypeHandler.DATATIME_PATTERN.matcher(value);
            result = matcher.matches();
        }
        return result;
    }
    
    @Override
    public String getRandomValue() {
        return TimeUtils.getDateFotmatYYMMDDHHmmSS();
    }
}
