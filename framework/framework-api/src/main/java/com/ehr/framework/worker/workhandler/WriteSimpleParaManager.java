package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.SimpleParaManager;

/**
 * 在线程中存放,读取,清除SimplePara参数的管理类
 * @author zoe
 */
public interface WriteSimpleParaManager extends SimpleParaManager{
    
    public void openThreadLocal(String parameter);

    public void closeThreadLocal();
}
