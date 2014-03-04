package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import java.util.Map;

/**
 *
 * @author zoe
 */
public interface DictionaryContextBuilder<T extends Entity> {
    
    public DynamicDictionaryHandler<T> getDynamicDictionaryHandler(final String dynamicDictionaryName);
    
    public Map<String, DynamicDictionaryHandler<T>> getDynamicDictionaryHandlerMap();
    
    public boolean assertExistDynamicDictionary(final String dynamicDictionaryName);

    public void putDynamicDictionaryHandler(final String dynamicDictionaryName, final DynamicDictionaryHandler<T> dynamicDictionaryHandler);
}
