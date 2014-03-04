package com.ehr.framework.entity.field;

import com.ehr.framework.context.DynamicDictionaryManager;
import com.ehr.framework.entity.field.filter.Filter;
import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;
import java.util.Map;

/**
 * 数据字典类型处理类
 * @author zoe
 */
public final class DynamicDictionaryFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    private final DynamicDictionaryManager dynamicDictionaryManager;
    private final Filter[] filters;

    protected DynamicDictionaryFieldHandlerImpl(final String name, final String exportName, final Filter[] filters, final String dynamicDictionaryName, final DynamicDictionaryManager dynamicDictionaryManager, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue) {
        super(name, exportName, typeHandler, fieldType, dynamicDictionaryName, defaultValue);
        this.dynamicDictionaryManager = dynamicDictionaryManager;
        this.filters = filters;
    }

    @Override
    public String getJson(final String value) {
        String result;
        String dicValue = this.getDicValue(value);
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() * 2 + value.length() + dicValue.length() + 12);
        jsonBuilder.append('"').append(this.name).append("\":").append(value).append(",\"").append(this.name).append("Dic\":\"").append(dicValue).append('"');
        result = jsonBuilder.toString();
        return result;
    }

    @Override
    public String getJsonNoDictionary(String value) {
        String result;
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() + value.length() + 5);
        jsonBuilder.append('"').append(this.name).append("\":").append(value).append('"');
        result = jsonBuilder.toString();
        return result;
    }

    @Override
    public String getExcelCellValue(final String value) {
        return this.getDicValue(value);
    }

    private String getDicValue(final String value) {
        String dicValue = "";
        //一定要判断非empty
        if (!value.isEmpty()) {
            final Map<String, String> dictionaryMap = this.dynamicDictionaryManager.getThreadLocal(this.dynamicDictionaryName);
            dicValue = dictionaryMap.get(value);
            if (dicValue == null) {
                dicValue = "";
            } else {
                for (Filter filter : filters) {
                    dicValue = filter.doFilter(dicValue);
                }
            }
        }
        return dicValue;
    }

    @Override
    public String getDictionaryKey(String eleNames) {
        return "-1";
    }

    @Override
    public String validate(String value) {
        return this.typeHandler.validate(value);
    }

    @Override
    public String getRandomValue() {
        return "-1";
    }
}
