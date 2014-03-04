package com.ehr.framework.context;

import java.util.Map;

/**
 * 在线程中存放,读取,清除SimpleMap参数的管理类
 * @author zoe
 */
public interface SimpleMapManager {

    public Map<String, String> getThreadLocal();
}
