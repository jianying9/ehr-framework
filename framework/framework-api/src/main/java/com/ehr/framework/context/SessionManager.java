package com.ehr.framework.context;

import com.ehr.framework.worker.workhandler.UserInfoEntity;

/**
 * session管理对象
 * @author zoe
 */
public interface SessionManager {

    /**
     * 从当前线程中获取登录信息
     * @return 
     */
    public UserInfoEntity getThreadLocal();
}
