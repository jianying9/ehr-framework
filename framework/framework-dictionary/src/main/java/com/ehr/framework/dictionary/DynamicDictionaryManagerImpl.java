package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.worker.workhandler.WriteDynamicDictionaryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态数据字典管理类
 * @author zoe
 */
public final class DynamicDictionaryManagerImpl<T extends Entity> implements WriteDynamicDictionaryManager{

    private final Map<String, DynamicDictionaryHandler<T>> dynamicDictionaryHandlerMap;
    private final ThreadLocal<Map<String, Map<String, String>>> threadLocal = new ThreadLocal<Map<String, Map<String, String>>>();

    public DynamicDictionaryManagerImpl(final Map<String, DynamicDictionaryHandler<T>> dynamicDictionaryHandlerMap) {
        this.dynamicDictionaryHandlerMap = dynamicDictionaryHandlerMap;
    }

    @Override
    public void openThreadLocal(final Map<String, Map<String, String>> dictionaryMap) {
        this.threadLocal.set(dictionaryMap);
    }

    @Override
    public void closeThreadLocal() {
        this.threadLocal.remove();
    }

    @Override
    public Map<String, String> getThreadLocal(final String dynamicDictionaryName) {
        return this.threadLocal.get().get(dynamicDictionaryName);
    }

    @Override
    public Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final List<String> keyList) {
        DynamicDictionaryHandler dynamicDictionaryHandler = this.dynamicDictionaryHandlerMap.get(dynamicDictionaryName);
        List<Long> keyListTemp = new ArrayList(keyList.size());
        for (String key : keyList) {
            keyListTemp.add(Long.parseLong(key));
        }
        return dynamicDictionaryHandler.getDictionaryValues(keyListTemp);
    }

    @Override
    public Map<String, String> getDictionaryValues(final String dynamicDictionaryName, final String key) {
        DynamicDictionaryHandler dynamicDictionaryHandler = this.dynamicDictionaryHandlerMap.get(dynamicDictionaryName);
        return dynamicDictionaryHandler.getDictionaryValues(Long.parseLong(key));
    }
}
