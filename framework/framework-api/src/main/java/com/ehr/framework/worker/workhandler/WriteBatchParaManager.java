package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.BatchParaManager;

/**
 * 在线程中存放,读取,清除BatchPara参数的管理类
 * @author zoe
 */
public interface WriteBatchParaManager extends BatchParaManager{

    public void openThreadLocal(String[] parameter);

    public void closeThreadLocal();
}
