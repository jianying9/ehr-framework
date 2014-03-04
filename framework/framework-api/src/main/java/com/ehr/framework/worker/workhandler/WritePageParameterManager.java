package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.PageParameterManager;

/**
 * 存放,读取,清除分页参数的工具类
 * @author zoe
 */
public interface WritePageParameterManager extends PageParameterManager {

    public void openThreadLocal(PageExtendedEntity pageExtendedEntity);

    public void closeThreadLocal();
}
