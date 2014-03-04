package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;

/**
 * 数字类型处理类
 * @author zoe
 */
public final class NumberFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    NumberFieldHandlerImpl(final String name, final String exportName, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue) {
        super(name, exportName, typeHandler, fieldType, "", defaultValue);
    }

    @Override
    public String getJson(final String value) {
        String result;
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() + value.length() + 3);
        jsonBuilder.append('"').append(this.name).append("\":").append(value);
        result = jsonBuilder.toString();
        return result;
    }

    @Override
    public String getJsonNoDictionary(String value) {
        return this.getJson(value);
    }

    @Override
    public String getExcelCellValue(final String value) {
        return value;
    }

    @Override
    public String getDictionaryKey(final String eleNames) {
        String result;
        if (eleNames.isEmpty()) {
            result = "0";
        } else {
            result = eleNames;
        }
        return result;
    }

    @Override
    public String validate(final String value) {
        return this.typeHandler.validate(value);
    }
    
    @Override
    public String getRandomValue() {
        return this.typeHandler.getRandomValue();
    }
}
