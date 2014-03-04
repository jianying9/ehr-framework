package com.ehr.framework.context;

/**
 * 在线程中存放,读取,清除BatchPara参数的管理类
 * @author zoe
 */
public interface BatchParaManager {

    public String[] getThreadLocal();
}
