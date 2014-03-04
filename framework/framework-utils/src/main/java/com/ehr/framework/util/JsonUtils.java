package com.ehr.framework.util;

import com.ehr.framework.entity.field.FieldHandler;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zoe
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    public static String valueToJSON(final String value, final FieldHandler fieldHandler) {
        String jsonStr = "{}";//return
        StringBuilder jsonBuilder = new StringBuilder(32);
        jsonBuilder.append('{').append(fieldHandler.getJson(value)).append('}');
        jsonStr = jsonBuilder.toString();
        return jsonStr;
    }

    public static String valueNoDictionaryToJSON(final String value, final FieldHandler fieldHandler) {
        String jsonStr = "{}";//return
        StringBuilder jsonBuilder = new StringBuilder(32);
        jsonBuilder.append('{').append(fieldHandler.getJsonNoDictionary(value)).append('}');
        jsonStr = jsonBuilder.toString();
        return jsonStr;
    }

    public static String valuesToJSON(final String[] values, final FieldHandler fieldHandler) {
        String jsonStr = "{}";//return
        if (values != null) {
            StringBuilder jsonBuilder = new StringBuilder(values.length * 32);
            for (String value : values) {
                jsonBuilder.append('{').append(fieldHandler.getJson(value)).append("},");
            }
            jsonBuilder.setLength(jsonBuilder.length() - 1);
            jsonStr = jsonBuilder.toString();
        }
        return jsonStr;
    }

    public static String valuesNoDictionaryToJSON(final String[] values, final FieldHandler fieldHandler) {
        String jsonStr = "{}";//return
        if (values != null) {
            StringBuilder jsonBuilder = new StringBuilder(values.length * 32);
            for (String value : values) {
                jsonBuilder.append('{').append(fieldHandler.getJsonNoDictionary(value)).append("},");
            }
            jsonBuilder.setLength(jsonBuilder.length() - 1);
            jsonStr = jsonBuilder.toString();
        }
        return jsonStr;
    }

    public static String mapToJSON(final Map<String, String> parameterMap, final String[] fieldNames, final Map<String, FieldHandler> fieldMap) {
        String jsonStr = "{}";//return
        if (parameterMap != null) {
            StringBuilder jsonBuilder = new StringBuilder(fieldNames.length * 32);
            JsonUtils.mapToJSON(parameterMap, fieldNames, fieldMap, jsonBuilder);
            jsonStr = jsonBuilder.toString();
        }
        return jsonStr;
    }

    public static String mapNoDictionaryToJSON(final Map<String, String> parameterMap, final String[] fieldNames, final Map<String, FieldHandler> fieldMap) {
        String jsonStr = "{}";//return
        if (parameterMap != null) {
            StringBuilder jsonBuilder = new StringBuilder(fieldNames.length * 32);
            JsonUtils.mapNoDictionaryToJSON(parameterMap, fieldNames, fieldMap, jsonBuilder);
            jsonStr = jsonBuilder.toString();
        }
        return jsonStr;
    }

    public static String mapListToJSON(List<Map<String, String>> parameterMapList, String[] fieldNames, Map<String, FieldHandler> fieldMap) {
        String jsonStr = "";//return
        if (parameterMapList != null && !parameterMapList.isEmpty()) {
            if (parameterMapList.size() == 1) {
                jsonStr = JsonUtils.mapToJSON(parameterMapList.get(0), fieldNames, fieldMap);
            } else {
                StringBuilder jsonBuilder = new StringBuilder(parameterMapList.size() * fieldNames.length * 32);
                for (Map<String, String> parameterMap : parameterMapList) {
                    JsonUtils.mapToJSON(parameterMap, fieldNames, fieldMap, jsonBuilder);
                    jsonBuilder.append(',');
                }
                jsonBuilder.setLength(jsonBuilder.length() - 1);
                jsonStr = jsonBuilder.toString();
            }
        }
        return jsonStr;
    }

    public static String mapListNoDictionaryToJSON(List<Map<String, String>> parameterMapList, String[] fieldNames, Map<String, FieldHandler> fieldMap) {
        String jsonStr = "";//return
        if (parameterMapList != null && !parameterMapList.isEmpty()) {
            if (parameterMapList.size() == 1) {
                jsonStr = JsonUtils.mapNoDictionaryToJSON(parameterMapList.get(0), fieldNames, fieldMap);
            } else {
                StringBuilder jsonBuilder = new StringBuilder(parameterMapList.size() * fieldNames.length * 32);
                for (Map<String, String> parameterMap : parameterMapList) {
                    JsonUtils.mapNoDictionaryToJSON(parameterMap, fieldNames, fieldMap, jsonBuilder);
                    jsonBuilder.append(',');
                }
                jsonBuilder.setLength(jsonBuilder.length() - 1);
                jsonStr = jsonBuilder.toString();
            }
        }
        return jsonStr;
    }

    private static void mapToJSON(final Map<String, String> parameterMap, final String[] fieldNames, final Map<String, FieldHandler> fieldMap, final StringBuilder jsonBuilder) {
        String value;
        FieldHandler fieldHandler;
        boolean isExist = false;
        jsonBuilder.append('{');
        for (String fieldName : fieldNames) {
            value = parameterMap.get(fieldName);
            if (value != null) {
                fieldHandler = fieldMap.get(fieldName);
                if (fieldHandler != null) {
                    jsonBuilder.append(fieldHandler.getJson(value)).append(',');
                    isExist = true;
                }
            }
        }
        if (isExist) {
            jsonBuilder.setLength(jsonBuilder.length() - 1);
        }
        jsonBuilder.append('}');
    }

    private static void mapNoDictionaryToJSON(final Map<String, String> parameterMap, final String[] fieldNames, final Map<String, FieldHandler> fieldMap, final StringBuilder jsonBuilder) {
        String value;
        FieldHandler fieldHandler;
        boolean isExist = false;
        jsonBuilder.append('{');
        for (String fieldName : fieldNames) {
            value = parameterMap.get(fieldName);
            if (value != null) {
                fieldHandler = fieldMap.get(fieldName);
                if (fieldHandler != null) {
                    jsonBuilder.append(fieldHandler.getJsonNoDictionary(value)).append(',');
                    isExist = true;
                }
            }
        }
        if (isExist) {
            jsonBuilder.setLength(jsonBuilder.length() - 1);
        }
        jsonBuilder.append('}');
    }
}
