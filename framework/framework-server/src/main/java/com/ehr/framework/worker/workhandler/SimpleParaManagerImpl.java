package com.ehr.framework.worker.workhandler;

/**
 * 在线程中存放,读取,清除SimplePara参数的管理类
 * @author zoe
 */
public final class SimpleParaManagerImpl implements WriteSimpleParaManager{

    private final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    @Override
    public void openThreadLocal(String parameter) {
        threadLocal.set(parameter);
    }

    @Override
    public void closeThreadLocal() {
        threadLocal.remove();
    }

    @Override
    public String getThreadLocal() {
        return threadLocal.get();
    }
}
