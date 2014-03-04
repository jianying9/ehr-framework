package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;
import com.ehr.framework.util.NumberUtils;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 数据字典类型处理类
 *
 * @author zoe
 */
public final class DictionaryFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    private final Map<String, String> dictionaryMap;

    protected DictionaryFieldHandlerImpl(final String name, final String exportName, final Map<String, String> dictionaryMap, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue) {
        super(name, exportName, typeHandler, fieldType, "", defaultValue);
        this.dictionaryMap = dictionaryMap;
    }

    @Override
    public String getJson(final String value) {
        String result;
        String dicValue = "";
        if (!value.equals("-1")) {
            dicValue = this.getDicValue(value);
        }
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() * 2 + value.length() + dicValue.length() + 12);
        jsonBuilder.append('"').append(this.name).append("\":").append(value).append(",\"").append(this.name).append("Dic\":\"").append(dicValue).append('"');
        result = jsonBuilder.toString();
        return result;
    }

    @Override
    public String getJsonNoDictionary(String value) {
        String result;
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() + value.length() + 5);
        jsonBuilder.append('"').append(this.name).append("\":\"").append(value).append('"');
        result = jsonBuilder.toString();
        return result;
    }

    @Override
    public String getExcelCellValue(final String value) {
        return this.getDicValue(value);
    }

    private String getDicValue(final String value) {
        String dicValue = "";
        if (!value.isEmpty()) {
            dicValue = this.dictionaryMap.get(value);
            if (dicValue == null) {
                dicValue = "";
            }
        }
        return dicValue;
    }

    @Override
    public String getDictionaryKey(String eleNames) {
        String eleValues = "-1";
        if (!eleNames.isEmpty()) {
            Set<Entry<String, String>> set = this.dictionaryMap.entrySet();
            for (Entry<String, String> entry : set) {
                if (entry.getValue().equals(eleNames)) {
                    eleValues = entry.getKey();
                    break;
                }
            }
        }
        return eleValues;
    }

    @Override
    public String validate(String value) {
        String result = "";
        if (!value.equals("-1") && !this.dictionaryMap.containsKey(value)) {
            StringBuilder errorMessage = new StringBuilder(36);
            errorMessage.append("'s dictionary group can not find key:").append(value);
            result = errorMessage.toString();
        }
        return result;
    }

    @Override
    public String getRandomValue() {
        String randomValue = "-1";
        if (this.dictionaryMap.size() > 0) {
            int valueIndex = NumberUtils.getRandomIntegerValue(this.dictionaryMap.size());
            Set<String> keySet = this.dictionaryMap.keySet();
            int index = 0;
            for (String key : keySet) {
                if (index == valueIndex) {
                    randomValue = key;
                    break;
                } else {
                    index++;
                }
            }
        }
        return randomValue;
    }
}
