package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.DynamicDictionaryManager;
import java.util.Map;

/**
 * 动态数据字典管理类
 * @author zoe
 */
public interface WriteDynamicDictionaryManager extends DynamicDictionaryManager{
    
    public void openThreadLocal(final Map<String, Map<String, String>> dictionaryMap);

    public void closeThreadLocal();
}
