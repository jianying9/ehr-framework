package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;

/**
 * jsonArray类型处理类
 *
 * @author zoe
 */
public final class JsonArrayFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    private final String jsonArrayTemp;

    JsonArrayFieldHandlerImpl(final String name, final String exportName, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue, final String jsonArrayTemp) {
        super(name, exportName, typeHandler, fieldType, "", defaultValue);
        this.jsonArrayTemp = jsonArrayTemp;
    }

    @Override
    public String getJson(final String value) {
        String result;
        String filterValue = value;
        if (filterValue.isEmpty()) {
            filterValue = "[]";
        }
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() + filterValue.length() + 3);
        jsonBuilder.append('"').append(name).append("\":").append(filterValue);
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
    public String getDictionaryKey(String eleNames) {
        return eleNames;
    }

    @Override
    public String validate(String value) {
        return this.typeHandler.validate(value);
    }

    @Override
    public String getRandomValue() {
        return this.jsonArrayTemp;
    }
}
