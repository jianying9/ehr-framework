package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.response.ResponseDataStateEnum;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 动态数据字典处理类
 * @author zoe
 */
;

public class DynamicDictionaryWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final WorkHandler responseWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final Set<String> dynamicFieldSet;
    private final WriteDynamicDictionaryManager writeDynamicDictionaryManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;

    public DynamicDictionaryWorkHandlerImpl(final WorkHandler responseWorkHandler, final String[] returnParameter, final Set<String> dynamicFieldSet, final WriteDynamicDictionaryManager writeDynamicDictionaryManager, final Map<String, FieldHandler> fieldHandlerMap, final HttpServletRequestManager httpServletRequestManager, final ResponseWriterManager responseWriterManager, final WorkHandler nextWorkHandler) {
        this.nextWorkHandler = nextWorkHandler;
        this.returnParameter = returnParameter;
        this.writeDynamicDictionaryManager = writeDynamicDictionaryManager;
        this.dynamicFieldSet = dynamicFieldSet;
        this.fieldHandlerMap = fieldHandlerMap;
        this.responseWorkHandler = responseWorkHandler;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
    }

    @Override
    public void execute() {
        this.nextWorkHandler.execute();
        final ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
        final ResponseDataStateEnum responseDataStateEnum = responseWriter.getResponseDataStateEnum();
        if (responseDataStateEnum == ResponseDataStateEnum.NO_DATA) {
            //无数据
            this.responseWorkHandler.execute();
        } else {
            HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
            String[] returnNames = this.returnParameter;
            String columnName = request.getParameter("returnNames");
            if (columnName != null) {
                returnNames = columnName.split(",");
            }
            //判断返回列是否存在动态数据字典
            final List<String> dynamicFiledList = new ArrayList<String>(this.dynamicFieldSet.size());
            for (String returnFieldName : returnNames) {
                if (this.dynamicFieldSet.contains(returnFieldName)) {
                    dynamicFiledList.add(returnFieldName);
                }
            }
            if (dynamicFiledList.isEmpty()) {
                //返回内容不包含动态数据字典
                this.responseWorkHandler.execute();
            } else {
                final String[] dynamicFieldArray = dynamicFiledList.toArray(new String[dynamicFiledList.size()]);
                if (responseDataStateEnum == ResponseDataStateEnum.MAP_DATA) {
                    //mapData
                    this.mapDataHandle(responseWriter.getMapData(), dynamicFieldArray);
                } else {
                    //mapListData
                    this.mapListDataHandle(responseWriter.getMapListData(), dynamicFieldArray);
                }
            }
        }
    }

    /**
     * 判断mapData数据中是否包含动态数据字典
     * @param mapData
     * @param dynamicFieldArray 
     */
    private void mapDataHandle(final Map<String, String> mapData, final String[] dynamicFieldArray) {
        if (dynamicFieldArray.length == 1) {
            this.validateMapDataSimpleDynamicDictionary(mapData, dynamicFieldArray[0]);
        } else {
            //获取数据中包含的动态数据字典字段集合
            final List<String> dynamicFiledList = new ArrayList<String>(dynamicFieldArray.length);
            for (String fieldName : dynamicFieldArray) {
                if (mapData.containsKey(fieldName)) {
                    dynamicFiledList.add(fieldName);
                }
            }
            if (dynamicFiledList.size() == 1) {
                this.validateMapDataSimpleDynamicDictionary(mapData, dynamicFiledList.get(0));
            } else {
                //判断动态数据字典类型是否一致
                final String dynamicDictionaryName = this.fieldHandlerMap.get(dynamicFiledList.get(0)).dynamicDictionaryName();
                boolean isTheSame = true;
                String tempDynamicDictionaryName;
                for (int index = 1; index < dynamicFiledList.size(); index++) {
                    tempDynamicDictionaryName = this.fieldHandlerMap.get(dynamicFiledList.get(index)).dynamicDictionaryName();
                    if (!tempDynamicDictionaryName.equals(dynamicDictionaryName)) {
                        isTheSame = false;
                        break;
                    }
                }
                String[] newDynamicFiledArray = dynamicFiledList.toArray(new String[dynamicFiledList.size()]);
                if (isTheSame) {
                    this.validateMapDataSimpleDynamicDictionary(mapData, newDynamicFiledArray, dynamicDictionaryName);
                } else {
                    this.validateMapDataMultiDynamicDictionary(mapData, newDynamicFiledArray);
                }
            }
        }
    }

    /**
     * 单动态数据字典,单个列
     * @param mapData
     * @param dynamicField 
     */
    private void validateMapDataSimpleDynamicDictionary(final Map<String, String> mapData, final String dynamicField) {
        String fieldValue = mapData.get(dynamicField);
        if (fieldValue == null || fieldValue.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
            final String dynamicDictionaryName = this.fieldHandlerMap.get(dynamicField).dynamicDictionaryName();
            this.dynamicDictionarySimpleValueHandle(dictionaryMaps, dynamicDictionaryName, fieldValue);
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }

    /**
     * 单动态数据字典,多个列
     * @param mapData
     * @param dynamicFieldArray
     * @param dynamicDictionaryName 
     */
    private void validateMapDataSimpleDynamicDictionary(final Map<String, String> mapData, final String[] dynamicFieldArray, final String dynamicDictionaryName) {
        String fieldValue;
        List<String> fieldValueList = new ArrayList<String>(dynamicFieldArray.length);
        for (String fieldName : dynamicFieldArray) {
            fieldValue = mapData.get(fieldName);
            //一定要判断非empty
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fieldValueList.add(fieldValue);
            }
        }
        if (fieldValueList.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
            this.dynamicDictionaryMultiValueHandle(dictionaryMaps, dynamicDictionaryName, fieldValueList);
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }

    /**
     * 多动态数据字典,多个列
     * @param mapData
     * @param dynamicFieldArray 
     */
    private void validateMapDataMultiDynamicDictionary(final Map<String, String> mapData, final String[] dynamicFieldArray) {
        //根据相同类型数据字典分类
        final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
        String fieldValue;
        List<String> fieldValueList = new ArrayList<String>(dynamicFieldArray.length);
        List<String> dynamicFieldList = new ArrayList<String>(dynamicFieldArray.length);
        Set<String> dynamicDicNameSet = new HashSet<String>(8, 1);
        for (String dynamicField : dynamicFieldArray) {
            dynamicDicNameSet.add(this.fieldHandlerMap.get(dynamicField).dynamicDictionaryName());
        }
        for (String dynamicDicName : dynamicDicNameSet) {
            for (String fieldName : dynamicFieldArray) {
                if (this.fieldHandlerMap.get(fieldName).dynamicDictionaryName().equals(dynamicDicName)) {
                    dynamicFieldList.add(fieldName);
                }
            }
            if (!dynamicFieldList.isEmpty()) {
                for (String fieldName : dynamicFieldList) {
                    fieldValue = mapData.get(fieldName);
                    //一定要判断非empty
                    if (fieldValue != null && !fieldValue.isEmpty()) {
                        fieldValueList.add(fieldValue);
                    }
                }
                if (!fieldValueList.isEmpty()) {
                    this.dynamicDictionaryMultiValueHandle(dictionaryMaps, dynamicDicName, fieldValueList);
                    fieldValueList.clear();
                }
                dynamicFieldList.clear();
            }
        }
        if (dictionaryMaps.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }

    /**
     * 单动态数据字典,单值
     * @param mapData
     * @param dynamicField
     * @param fieldValue 
     */
    private void dynamicDictionarySimpleValueHandle(final Map<String, Map<String, String>> dictionaryMaps, final String dynamicDictionaryName, final String fieldValue) {
        Map<String, String> dictionaryMap = this.writeDynamicDictionaryManager.getDictionaryValues(dynamicDictionaryName, fieldValue);
        dictionaryMaps.put(dynamicDictionaryName, dictionaryMap);
    }

    private void dynamicDictionaryMultiValueHandle(final Map<String, Map<String, String>> dictionaryMaps, final String dynamicDictionaryName, final List<String> fieldValueList) {
        Map<String, String> dictionaryMap = this.writeDynamicDictionaryManager.getDictionaryValues(dynamicDictionaryName, fieldValueList);
        dictionaryMaps.put(dynamicDictionaryName, dictionaryMap);
    }

    private void mapListDataHandle(final List<Map<String, String>> mapListData, final String[] dynamicFieldArray) {
        if (mapListData.isEmpty()) {
            this.responseWorkHandler.execute();
        } else if (mapListData.size() == 1) {
            this.mapDataHandle(mapListData.get(0), dynamicFieldArray);
        } else {
            //获取数据中包含的动态数据字典字段集合
            final List<String> dynamicFiledList = new ArrayList<String>(dynamicFieldArray.length);
            for (String fieldName : dynamicFieldArray) {
                if (!dynamicFiledList.contains(fieldName)) {
                    for (Map<String, String> mapData : mapListData) {
                        if (mapData.containsKey(fieldName)) {
                            dynamicFiledList.add(fieldName);
                            break;
                        }
                    }
                }
            }
            if (dynamicFiledList.isEmpty()) {
                this.responseWorkHandler.execute();
            } else {
                if (dynamicFiledList.size() == 1) {
                    this.validateMapListDataSimpleDynamicDictionary(mapListData, dynamicFiledList.get(0));
                } else {
                    //判断动态数据字典类型是否一致
                    final String dynamicDictionaryName = this.fieldHandlerMap.get(dynamicFiledList.get(0)).dynamicDictionaryName();
                    boolean isTheSame = true;
                    for (int index = 1; index < dynamicFiledList.size(); index++) {
                        if (!this.fieldHandlerMap.get(dynamicFiledList.get(index)).dynamicDictionaryName().equals(dynamicDictionaryName)) {
                            isTheSame = false;
                            break;
                        }
                    }
                    String[] newDynamicFiledArray = dynamicFiledList.toArray(new String[dynamicFiledList.size()]);
                    if (isTheSame) {
                        this.validateMapListDataSimpleDynamicDictionary(mapListData, newDynamicFiledArray, dynamicDictionaryName);
                    } else {
                        this.validateMapListDataMultiDynamicDictionary(mapListData, newDynamicFiledArray);
                    }
                }
            }
        }
    }

    private void validateMapListDataSimpleDynamicDictionary(final List<Map<String, String>> mapListData, final String dynamicField) {
        List<String> fieldValueList = new ArrayList<String>(mapListData.size());
        String fieldValue;
        for (Map<String, String> mapData : mapListData) {
            fieldValue = mapData.get(dynamicField);
            //一定要判断非empty
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fieldValueList.add(fieldValue);
            }
        }
        if (fieldValueList.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
            final String dynamicDictionaryName = this.fieldHandlerMap.get(dynamicField).dynamicDictionaryName();
            this.dynamicDictionaryMultiValueHandle(dictionaryMaps, dynamicDictionaryName, fieldValueList);
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }

    private void validateMapListDataSimpleDynamicDictionary(final List<Map<String, String>> mapListData, final String[] dynamicFieldArray, final String dynamicDictionaryName) {
        String fieldValue;
        List<String> fieldValueList = new ArrayList<String>(dynamicFieldArray.length);
        for (String fieldName : dynamicFieldArray) {
            for (Map<String, String> mapData : mapListData) {
                fieldValue = mapData.get(fieldName);
                //一定要判断非empty
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fieldValueList.add(fieldValue);
                }
            }
        }
        if (fieldValueList.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
            this.dynamicDictionaryMultiValueHandle(dictionaryMaps, dynamicDictionaryName, fieldValueList);
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }

    private void validateMapListDataMultiDynamicDictionary(final List<Map<String, String>> mapListData, final String[] dynamicFieldArray) {
        //根据相同类型数据字典分类
        final Map<String, Map<String, String>> dictionaryMaps = new HashMap<String, Map<String, String>>(8, 1);
        String fieldValue;
        List<String> fieldValueList = new ArrayList<String>(dynamicFieldArray.length);
        List<String> dynamicFieldList = new ArrayList<String>(dynamicFieldArray.length);
        Set<String> dynamicDicNameSet = new HashSet<String>(8, 1);
        for (String dynamicField : dynamicFieldArray) {
            dynamicDicNameSet.add(this.fieldHandlerMap.get(dynamicField).dynamicDictionaryName());
        }
        for (String dynamicDicName : dynamicDicNameSet) {
            for (String fieldName : dynamicFieldArray) {
                if (this.fieldHandlerMap.get(fieldName).dynamicDictionaryName().equals(dynamicDicName)) {
                    dynamicFieldList.add(fieldName);
                }
            }
            if (!dynamicFieldList.isEmpty()) {
                for (String fieldName : dynamicFieldArray) {
                    for (Map<String, String> mapData : mapListData) {
                        fieldValue = mapData.get(fieldName);
                        //一定要判断非empty
                        if (fieldValue != null && !fieldValue.isEmpty()) {
                            fieldValueList.add(fieldValue);
                        }
                    }
                }
                if (!fieldValueList.isEmpty()) {
                    this.dynamicDictionaryMultiValueHandle(dictionaryMaps, dynamicDicName, fieldValueList);
                    fieldValueList.clear();
                }
                dynamicFieldList.clear();
            }
        }
        if (dictionaryMaps.isEmpty()) {
            this.responseWorkHandler.execute();
        } else {
            this.writeDynamicDictionaryManager.openThreadLocal(dictionaryMaps);
            this.responseWorkHandler.execute();
            this.writeDynamicDictionaryManager.closeThreadLocal();
        }
    }
}
