package com.ehr.framework.worker.workhandler;

import java.util.List;
import java.util.Map;

/**
 * excel数据保存对象
 * @author zoe
 */
public interface ExcelData {

    /**
     * 判断是否存在扩展字段
     * @return 
     */
    public boolean containsExtendedColumn();

    /**
     * 获取扩展字段数量
     * @return 
     */
    public int getExtendedColumnNum();

    /**
     * 获取扩展字段的excel列头信息
     * @return 
     */
    public Map<String, String> getExtendedHeader();
    
    /**
     * 返回扩张字段
     * @return 
     */
    public String[] getExtendedColumns();

    /**
     * 获取所有数据
     * @return 
     */
    public List<Map<String, String>> getAllData();

    /**
     * 获取扩展数据
     * @return 
     */
    public List<Map<String, String>> getExtendedData();

    /**
     * 获取固定的数据
     * @return 
     */
    public List<Map<String, String>> getData();
}
