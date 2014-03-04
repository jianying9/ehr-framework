package com.ehr.framework.worker.workhandler;

/**
 * 存放,读取,清除分页参数的工具类
 * @author zoe
 */
public final class PageParameterManagerImpl implements WritePageParameterManager{

    private final ThreadLocal<PageExtendedEntity> threadLocal = new ThreadLocal<PageExtendedEntity>();

    @Override
    public void openThreadLocal(PageExtendedEntity pageExtendedEntity) {
        threadLocal.set(pageExtendedEntity);
    }

    @Override
    public void closeThreadLocal() {
        threadLocal.remove();
    }

    @Override
    public PageExtendedEntity getThreadLocal() {
        return threadLocal.get();
    }
}
