package com.ehr.framework.worker.workhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel数据保存对象
 * @author zoe
 */
public class ExcelDataImpl implements ExcelData {

    private final List<Map<String, String>> allDataMapList;
    private final Map<String, String> extendedHeader;
    private final String[] importantParameter;

    public ExcelDataImpl(final List<Map<String, String>> allDataMapList, final Map<String, String> extendedHeader, final String[] importantParameter) {
        this.allDataMapList = allDataMapList;
        this.extendedHeader = extendedHeader;
        this.importantParameter = importantParameter;
    }

    /**
     * 判断是否存在扩展字段
     * @return 
     */
    @Override
    public boolean containsExtendedColumn() {
        return !this.extendedHeader.isEmpty();
    }

    /**
     * 获取扩展字段数量
     * @return 
     */
    @Override
    public int getExtendedColumnNum() {
        return this.extendedHeader.size();
    }

    /**
     * 获取扩展字段的excel列头信息
     * @return 
     */
    @Override
    public Map<String, String> getExtendedHeader() {
        return this.extendedHeader;
    }

    /**
     * 获取所有数据
     * @return 
     */
    @Override
    public List<Map<String, String>> getAllData() {
        return this.allDataMapList;
    }

    /**
     * 获取扩展数据
     * @return 
     */
    @Override
    public List<Map<String, String>> getExtendedData() {
        List<Map<String, String>> extendedData = new ArrayList<Map<String, String>>(this.allDataMapList.size());
        Map<String, String> extendedMap;
        for (Map<String, String> allDataMap : this.allDataMapList) {
            extendedMap = new HashMap<String, String>(this.extendedHeader.size(), 1);
            int extendedColumnNum = this.extendedHeader.size();
            String[] extendedColumns = this.getExtendedColumns();
            String parameterName;
            String parameterValue;
            for (int extendedIndex = 0; extendedIndex < extendedColumns.length && extendedIndex < extendedColumnNum; extendedIndex++) {
                parameterName = extendedColumns[extendedIndex];
                parameterValue = allDataMap.get(parameterName);
                extendedMap.put(parameterName, parameterValue);
            }
            extendedData.add(extendedMap);
        }
        return extendedData;
    }

    @Override
    public String[] getExtendedColumns() {
        String[] result = {};
        if (!this.extendedHeader.isEmpty()) {
            result = this.extendedHeader.keySet().toArray(new String[this.extendedHeader.size()]);
        }
        return result;
    }

    /**
     * 获取固定的数据
     * @return 
     */
    @Override
    public List<Map<String, String>> getData() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>(this.allDataMapList.size());
        Map<String, String> dataMap;
        for (Map<String, String> allDataMap : this.allDataMapList) {
            dataMap = new HashMap<String, String>(this.importantParameter.length, 1);
            String parameterValue;
            for (String parameterName : this.importantParameter) {
                parameterValue = allDataMap.get(parameterName);
                dataMap.put(parameterName, parameterValue);
            }
            data.add(dataMap);
        }
        return data;
    }
}
