package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.SessionManager;

/**
 * session管理对象
 *
 * @author zoe
 */
public interface WriteSessionManager extends SessionManager {

    public void openThreadLocal(UserInfoEntity userInfoEntity);

    public void closeThreadLocal();
}
