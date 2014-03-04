package com.ehr.framework.context;

import com.ehr.framework.worker.workhandler.PageExtendedEntity;

/**
 * 存放,读取,清除分页参数的工具类
 * @author zoe
 */
public interface PageParameterManager {

    public PageExtendedEntity getThreadLocal();
}
