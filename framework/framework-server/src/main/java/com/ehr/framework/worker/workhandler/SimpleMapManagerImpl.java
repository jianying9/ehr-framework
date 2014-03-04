package com.ehr.framework.worker.workhandler;

import java.util.Map;

/**
 * 在线程中存放,读取,清除SimpleMap参数的管理类
 * @author zoe
 */
public final class SimpleMapManagerImpl implements WriteSimpleMapManager{

    private final ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>();

    @Override
    public void openThreadLocal(Map<String, String> parameterMap) {
        threadLocal.set(parameterMap);
    }

    @Override
    public void closeThreadLocal() {
        threadLocal.remove();
    }

    @Override
    public Map<String, String> getThreadLocal() {
        return threadLocal.get();
    }
}
