package com.ehr.framework.worker.workhandler;

/**
 * 存放,读取,清除session管理对象
 *
 * @author zoe
 */
public final class SessionManagerImpl implements WriteSessionManager {

    private final ThreadLocal<UserInfoEntity> threadLocal = new ThreadLocal<UserInfoEntity>();

    @Override
    public void openThreadLocal(UserInfoEntity userInfoEntity) {
        threadLocal.set(userInfoEntity);
    }

    @Override
    public void closeThreadLocal() {
        threadLocal.remove();
    }

    @Override
    public UserInfoEntity getThreadLocal() {
        return threadLocal.get();
    }
}
