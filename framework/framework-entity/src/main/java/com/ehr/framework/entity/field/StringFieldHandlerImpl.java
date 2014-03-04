package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.filter.Filter;
import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;

/**
 * 字符类型处理类
 * @author zoe
 */
public final class StringFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    private final Filter[] filters;

    StringFieldHandlerImpl(final String name, final String exportName, final Filter[] filters, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue) {
        super(name, exportName, typeHandler, fieldType, "", defaultValue);
        this.filters = filters;
    }

    @Override
    public String getJson(final String value) {
        String result;
        String filterValue = value;
        for (Filter filter : filters) {
            filterValue = filter.doFilter(filterValue);
        }
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() + filterValue.length() + 5);
        jsonBuilder.append('"').append(this.name).append("\":\"").append(filterValue).append('"');
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
        return eleNames;
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
