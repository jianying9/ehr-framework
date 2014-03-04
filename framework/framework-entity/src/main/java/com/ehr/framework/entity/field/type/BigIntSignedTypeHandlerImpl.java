package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 八字节有符号类型-9223372036854775808,9223372036854775807
 * @author zoe
 */
public final class BigIntSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be bigint signed";
    private final static Pattern BIG_INT_SIGNED_PATTERN = Pattern.compile("\\d|-[1-9]|[1-9]\\d{1,17}|-[1-9]\\d{1,17}|[1-8]\\d{18}|-[1-8]\\d{18}|-9223372036854775808|-?(?:9[0-1]\\d{17}|92[0-1]\\d{16}|922[0-2]\\d{15}|9223[0-2]\\d{14}|92233[0-6]\\d{13}|922337[0-1]\\d{12}|92233720[0-2]\\d{10}|922337203[0-5]\\d{9}|9223372036[0-7]\\d{8}|92233720368[0-4]\\d{7}|922337203685[0-3]\\d{6}|9223372036854[0-6]\\d{5}|92233720368547[0-6]\\d{4}|922337203685477[0-4]\\d{3}|9223372036854775[0-7]\\d{2}|922337203685477580[0-7])");

    @Override
    protected String getErrorMessage() {
        return BigIntSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        Matcher matcher = BigIntSignedTypeHandlerImpl.BIG_INT_SIGNED_PATTERN.matcher(value);
        return matcher.matches();
    }
    
    @Override
    public String getRandomValue() {
        long value = NumberUtils.getRandomLongValue();
        return Long.toString(value);
    }
}
