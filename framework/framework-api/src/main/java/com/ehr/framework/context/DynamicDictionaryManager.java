package com.ehr.framework.context;

import java.util.List;
import java.util.Map;

/**
 * 动态数据字典管理类
 * @author zoe
 */
public interface DynamicDictionaryManager {

    public Map<String, String> getThreadLocal(final String dynamicDictionaryName);

    public Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final List<String> keyList);

    public Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final String key);
}
