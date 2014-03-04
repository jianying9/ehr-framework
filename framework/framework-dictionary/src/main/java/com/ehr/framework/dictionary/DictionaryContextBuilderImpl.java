package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zoe
 */
public class DictionaryContextBuilderImpl<T extends Entity> implements DictionaryContextBuilder<T> {

    private final Map<String, DynamicDictionaryHandler<T>> dynamicDictionaryHandlerMap;

    public DictionaryContextBuilderImpl() {
        this.dynamicDictionaryHandlerMap = new HashMap<String, DynamicDictionaryHandler<T>>(16, 1);
    }

    @Override
    public void putDynamicDictionaryHandler(String dynamicDictionaryName, DynamicDictionaryHandler<T> dynamicDictionaryHandler) {
        if (this.dynamicDictionaryHandlerMap.containsKey(dynamicDictionaryName)) {
            throw new RuntimeException("There was an error putting dynamicDictionaryHandler. Cause: dynamicDictionaryName reduplicated, ".concat(dynamicDictionaryName));
        }
        this.dynamicDictionaryHandlerMap.put(dynamicDictionaryName, dynamicDictionaryHandler);
    }

    @Override
    public DynamicDictionaryHandler<T> getDynamicDictionaryHandler(final String dynamicDictionaryName) {
        return this.dynamicDictionaryHandlerMap.get(dynamicDictionaryName);
    }

    @Override
    public Map<String, DynamicDictionaryHandler<T>> getDynamicDictionaryHandlerMap() {
        return Collections.unmodifiableMap(this.dynamicDictionaryHandlerMap);
    }

    @Override
    public boolean assertExistDynamicDictionary(String dynamicDictionaryName) {
        return this.dynamicDictionaryHandlerMap.containsKey(dynamicDictionaryName);
    }
}
