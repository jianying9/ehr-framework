package com.ehr.framework.context;

import java.util.Map;

/**
 * 存放,读取,清除BatchMap参数的工具类
 * @author zoe
 */
public interface BatchMapManager {

    public Map<String, String[]> getThreadLocal();
}
