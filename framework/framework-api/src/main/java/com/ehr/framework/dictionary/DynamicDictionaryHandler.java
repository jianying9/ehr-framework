package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zoe
 */
public interface DynamicDictionaryHandler<T extends Entity> {

    public Map<String, String> getDictionaryValues(final List<Long> keyList);

    public Map<String, String> getDictionaryValues(final long key);
}
