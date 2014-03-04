package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.TimeUtils;
import java.util.regex.Matcher;

/**
 * 时间类型YYYY-MM-DD HH:MM,YYYY-MM-DD HH:MM:SS,可以为空字符
 *
 * @author zoe
 */
public final class DateTimeOrEmptyTypeHanderImpl extends AbstractDateTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be datetime or empty";

    @Override
    protected String getErrorMessage() {
        return DateTimeOrEmptyTypeHanderImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        boolean result = false;
        if (value.isEmpty()) {
            result = true;
        } else {
            if (value.length() > 10 && value.length() < 20) {
                Matcher matcher = AbstractDateTypeHandler.DATATIME_PATTERN.matcher(value);
                result = matcher.matches();
            }
        }
        return result;
    }

    @Override
    public String getRandomValue() {
        return TimeUtils.getDateFotmatYYMMDDHHmmSS();
    }
}
