package com.ehr.framework.entity.field.type;

import com.ehr.framework.util.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 四字节有符号类型-2147483648,2147483647
 * @author zoe
 */
public final class IntSignedTypeHandlerImpl extends AbstractNumberTypeHandler implements TypeHandler {

    private final static String ERROR_MESSAGE = " must be int signed";
    private final static Pattern INT_SIGNED_PATTERN = Pattern.compile("\\d|-[1-9]|[1-9]\\d{1,8}|1\\d{9}|-[1-9]\\d{1,8}|-1\\d{9}|-2147483648|-?(?:20\\d{8}|21[0-3]\\d{7}|214[0-6]\\d{6}|2147[0-3]\\d{5}|21474[0-7]\\d{4}|214748[0-2]\\d{3}|2147483[0-5]\\d{2}|21474836[0-3]\\d|214748364[0-7])");

    @Override
    protected String getErrorMessage() {
        return IntSignedTypeHandlerImpl.ERROR_MESSAGE;
    }

    @Override
    protected boolean patternValidate(final String value) {
        Matcher matcher = IntSignedTypeHandlerImpl.INT_SIGNED_PATTERN.matcher(value);
        return matcher.matches();
    }
    
    @Override
    public String getRandomValue() {
        int value = NumberUtils.getRandomIntegerValue();
        return Integer.toString(value);
    }
}
