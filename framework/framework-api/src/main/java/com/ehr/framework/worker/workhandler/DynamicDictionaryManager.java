package com.ehr.framework.worker.workhandler;

import java.util.List;
import java.util.Map;

/**
 * 动态数据字典管理类
 * @author zoe
 */
public interface DynamicDictionaryManager {
    
    public void openThreadLocal(final Map<String, Map<String, String>> dictionaryMap);

    public void closeThreadLocal();

    public Map<String, String> getThreadLocal(final String dynamicDictionaryName);

    Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final List<String> keyList);

    Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final String key);
}
