package com.ehr.framework.util;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.excel.ExcelWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author zoe
 */
public final class ExcelUtils {

    private ExcelUtils() {
    }

    private static String[][] mapToDimArray(final Map<String, String> parameterMap, final String[] fieldName, final Map<String, FieldHandler> fieldHandlerMap) {
        String[][] result = new String[1][];
        result[0] = mapToArray(parameterMap, fieldName, fieldHandlerMap);
        return result;
    }

    /**
     * 将parameterMapList对象转换成二维数组
     * @param parameterMap
     * @return String[][]
     */
    private static String[][] mapListToDimArray(final List<Map<String, String>> parameterMapList, final String[] fieldName, final Map<String, FieldHandler> fieldMap) {
        String[][] result = new String[parameterMapList.size()][fieldName.length];
        for (int index = 0; index < parameterMapList.size(); index++) {
            result[index] = mapToArray(parameterMapList.get(index), fieldName, fieldMap);
        }
        return result;
    }

    /**
     * 将parameterMap对象转换成一维数组
     * @param parameterMap
     * @param fieldName
     * @return String[]
     */
    private static String[] mapToArray(final Map<String, String> parameterMap, final String[] fieldNames, final Map<String, FieldHandler> fieldMap) {
        String[] result = new String[fieldNames.length];//return
        String fieldValue;
        String fieldName;
        FieldHandler fieldHandler;
        for (int index = 0; index < fieldNames.length; index++) {
            fieldName = fieldNames[index];
            fieldValue = parameterMap.get(fieldName);
            if (fieldValue != null) {
                if (!fieldValue.isEmpty()) {
                    fieldHandler = fieldMap.get(fieldName);
                    if (fieldHandler != null) {
                        fieldValue = fieldHandler.getExcelCellValue(fieldValue);
                    } else {
                        fieldValue = "";
                    }
                }
            } else {
                fieldValue = "";
            }
            result[index] = fieldValue;
        }
        return result;
    }

    public static ExcelWriter createExcelModelWriter(final String[] fieldNames, final Map<String, FieldHandler> fieldHandlerMap, final Map<String, String> extendedExcelColumnMap) {
        ExcelWriter excelWriter = new ExcelWriter();
        String[] excelHeader;
        FieldHandler fieldHandler;
        if (extendedExcelColumnMap.isEmpty()) {
            //没有扩展字段
            excelHeader = new String[fieldNames.length];
            for (int index = 0; index < fieldNames.length; index++) {
                fieldHandler = fieldHandlerMap.get(fieldNames[index]);
                if (fieldHandler == null) {
                    excelHeader[index] = fieldNames[index];
                } else {
                    excelHeader[index] = fieldHandler.getExportName();
                }
            }
        } else {
            //有扩展字段
            List<String> excelHeaderList = new ArrayList<String>(fieldNames.length + extendedExcelColumnMap.size());
            //获取固定字段
            for (String fieldName : fieldNames) {
                fieldHandler = fieldHandlerMap.get(fieldName);
                if (fieldHandler == null) {
                    excelHeaderList.add(fieldName);
                } else {
                    excelHeaderList.add(fieldHandler.getExportName());
                }
            }
            //获取扩展字段
            for (Entry<String, String> entry : extendedExcelColumnMap.entrySet()) {
                excelHeaderList.add(entry.getValue());
            }
            excelHeader = excelHeaderList.toArray(new String[excelHeaderList.size()]);
        }
        String[][] excelData = null;
        excelWriter.addSheet("", excelData, excelHeader);
        return excelWriter;
    }

    public static ExcelWriter createExcelWriter(final Map<String, String> parameterMap, final String[] fieldName, final Map<String, FieldHandler> fieldHandlerMap) {
        ExcelWriter excelWriter = new ExcelWriter();
        if (parameterMap != null) {
            String[][] excelData = ExcelUtils.mapToDimArray(parameterMap, fieldName, fieldHandlerMap);
            ExcelUtils.createExcelWriter(excelWriter, excelData, fieldName, fieldHandlerMap);
        }
        return excelWriter;
    }

    public static ExcelWriter createExcelWriter(final List<Map<String, String>> parameterMapList, final String[] fieldName, final Map<String, FieldHandler> fieldHandlerMap) {
        ExcelWriter excelWriter = new ExcelWriter();
        if (parameterMapList != null) {
            String[][] excelData = ExcelUtils.mapListToDimArray(parameterMapList, fieldName, fieldHandlerMap);
            ExcelUtils.createExcelWriter(excelWriter, excelData, fieldName, fieldHandlerMap);
        }
        return excelWriter;
    }

    private static void createExcelWriter(final ExcelWriter excelWriter, final String[][] excelData, final String[] fieldName, final Map<String, FieldHandler> fieldHandlerMap) {
        String[] excelHeader = new String[fieldName.length];
        FieldHandler fieldHandler;
        for (int index = 0; index < fieldName.length; index++) {
            fieldHandler = fieldHandlerMap.get(fieldName[index]);
            if (fieldHandler == null) {
                excelHeader[index] = fieldName[index];
            } else {
                excelHeader[index] = fieldHandler.getExportName();
            }
        }
        excelWriter.addSheet("", excelData, excelHeader);
    }
}
