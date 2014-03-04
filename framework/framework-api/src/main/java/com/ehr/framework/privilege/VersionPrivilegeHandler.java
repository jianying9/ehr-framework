package com.ehr.framework.privilege;

/**
 * 版本权限处理
 *
 * @author zoe
 */
public interface VersionPrivilegeHandler {

    /**
     * 是否可以访问该服务
     *
     * @param userId
     * @param actionName
     * @return
     */
    public boolean canAccessAction(long companyId, String actionName);
}
