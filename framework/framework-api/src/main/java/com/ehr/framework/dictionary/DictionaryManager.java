package com.ehr.framework.dictionary;

import java.util.Map;

/**
 *
 * @author zoe
 */
public interface DictionaryManager {

    /**
     * 获取数据字典集合
     * @param dicName 数据字典名称
     * @return 
     */
    public Map<String, String> getDictionaryMap(final String dicName);

    /**
     * 根据数据字典名称和key，获取对应的值
     * @param dicName 数据字典名称
     * @param eleValue 数据字典key
     * @return 
     */
    public String getDictionaryValue(String dicName, int eleValue);

    /**
     * 根据数据字典名称和key，获取对应的值
     * @param dicName 数据字典名称
     * @param eleValue 数据字典key
     * @return 
     */
    public String getDictionaryValue(String dicName, String eleValue);

    /**
     * 根据数据字典名称和key集合，获取对应值的集合
     * @param dicName 数据字典名称
     * @param eleValue 数据字典key
     * @return 
     */
    public String getMultiDictionaryValue(String dicName, String eleValues);

    /**
     * 根据数据字典名称和值，获取对应的key
     * @param dicName 数据字典名称
     * @param eleName 数据字典key
     * @return 
     */
    public String getDictionaryKeyValue(String dicName, String eleName);

    /**
     * 根据数据字典名称和值的集合，获取对应的key的集合
     * @param dicName 数据字典名称
     * @param eleNames 数据字典key
     * @return 
     */
    public String getMultiDictionaryKeyValue(String dicName, String eleNames);
}
