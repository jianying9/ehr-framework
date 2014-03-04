package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;
import com.ehr.framework.util.NumberUtils;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author zoe
 */
public final class DictionaryArrayFieldHandlerImpl extends AbstractFieldHandler implements FieldHandler {

    private final Map<String, String> dictionaryMap;

    protected DictionaryArrayFieldHandlerImpl(final String name, final String exportName, final Map<String, String> dictionaryMap, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String defaultValue) {
        super(name, exportName, typeHandler, fieldType, "", defaultValue);
        this.dictionaryMap = dictionaryMap;
    }

    @Override
    public String getJson(final String value) {
        String result;
        String eleValue = this.getDicValues(value);
        StringBuilder jsonBuilder = new StringBuilder(this.name.length() * 2 + value.length() + eleValue.length() + 14);
        jsonBuilder.append('"').append(this.name).append("\":\"").append(value).append("\",\"").append(this.name).append("Dic\":\"").append(eleValue).append('"');
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
        return this.getDicValues(value);
    }

    private String getDicValues(final String values) {
        String result = "";
        if (!values.isEmpty()) {
            String[] keys = values.split(",");
            if (keys.length == 1) {
                if (!keys[0].equals("-1")) {
                    result = this.dictionaryMap.get(keys[0]);
                    if (result == null) {
                        result = "";
                    }
                }
            } else {
                String eleName;
                boolean isExist = false;
                StringBuilder eleNamesBuilder = new StringBuilder(keys.length * 8);
                for (String key : keys) {
                    if (!key.equals("-1")) {
                        eleName = this.dictionaryMap.get(key);
                        if (eleName != null) {
                            eleNamesBuilder.append(eleName).append(',');
                            isExist = true;
                        }
                    }
                }
                if (isExist) {
                    eleNamesBuilder.setLength(eleNamesBuilder.length() - 1);
                }
                result = eleNamesBuilder.toString();
            }
        }
        return result;
    }

    @Override
    public String getDictionaryKey(final String eleNames) {
        String eleValues = "-1";
        if (!eleNames.isEmpty()) {
            Set<Entry<String, String>> set = this.dictionaryMap.entrySet();
            String[] eleNameArr = eleNames.split(",");
            if (eleNameArr.length == 1) {
                String eleName = eleNameArr[0];
                for (Entry<String, String> entry : set) {
                    if (entry.getValue().equals(eleName)) {
                        eleValues = entry.getKey();
                        break;
                    }
                }
            } else {
                StringBuilder eleValuesBuilder = new StringBuilder(eleNameArr.length * 2);
                String eleValue;
                boolean isExist = false;
                for (String eleName : eleNameArr) {
                    for (Entry<String, String> entry : set) {
                        if (entry.getValue().equals(eleName)) {
                            eleValue = entry.getKey();
                            eleValuesBuilder.append(eleValue).append(',');
                            isExist = true;
                            break;
                        }
                    }
                }
                if (isExist) {
                    eleValuesBuilder.setLength(eleValuesBuilder.length() - 1);
                }
                eleValues = eleValuesBuilder.toString();
            }
        }
        return eleValues;
    }

    @Override
    public String validate(String value) {
        String result = "";
        String[] keys = value.split(",");
        for (String key : keys) {
            if (!key.equals("-1") && !this.dictionaryMap.containsKey(key)) {
                StringBuilder errorMessage = new StringBuilder(64);
                errorMessage.append("'s dictionary group can not find key:").append(key);
                result = errorMessage.toString();
                break;
            }
        }
        return result;
    }

    @Override
    public String getRandomValue() {
        String randomValue = "-1";
        int valueIndex;
        if (this.dictionaryMap.size() > 0) {
            int length = NumberUtils.getRandomIntegerValue(3) + 1;
            StringBuilder valueBuilder = new StringBuilder(length * 5);
            Set<String> keySet = this.dictionaryMap.keySet();
            int index;
            for (int lengthIndex = 0; lengthIndex < length; lengthIndex++) {
                valueIndex = NumberUtils.getRandomIntegerValue(this.dictionaryMap.size());
                index = 0;
                for (String key : keySet) {
                    if (index == valueIndex) {
                        valueBuilder.append(key).append(',');
                        break;
                    } else {
                        index++;
                    }
                }
            }
            valueBuilder.setLength(valueBuilder.length() - 1);
            randomValue = valueBuilder.toString();
        }
        return randomValue;
    }
}
