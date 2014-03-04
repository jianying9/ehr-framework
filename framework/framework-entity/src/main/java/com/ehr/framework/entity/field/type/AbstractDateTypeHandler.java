package com.ehr.framework.entity.field.type;

import com.ehr.framework.entity.field.FieldTypeClassEnum;
import java.util.regex.Pattern;

/**
 * 正则类型验证抽象类
 * @author zoe
 */
public abstract class AbstractDateTypeHandler extends AbstractTypeHandler {

    protected final static Pattern DATA_PATTERN = Pattern.compile("[1-9]\\d{3}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|3[0-1])");
    protected final static Pattern DATATIME_PATTERN = Pattern.compile("[1-9]\\d{3}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|3[0-1]) (?:[0-1]\\d|2[0-3]):[0-5]\\d(?::[0-5]\\d)?");

    @Override
    public final String getDefaultValue() {
        return TypeHandler.DEFAULT_DATE_VALUE;
    }

    @Override
    public final FieldTypeClassEnum getFieldTypeClassEnum() {
        return FieldTypeClassEnum.DATE;
    }

    protected abstract boolean patternValidate(String value);

    public final String validate(final String value) {
        return this.patternValidate(value) ? "" : this.getErrorMessage();
    }

    public final String formatValue(final String value) {
        String result = value;
        if (value == null || !this.validate(value).isEmpty()) {
            result = TypeHandler.DEFAULT_DATE_VALUE;
        }
        return result;
    }
}
