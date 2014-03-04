package com.ehr.framework.privilege;

/**
 * 用户自定义权限处理
 *
 * @author zoe
 */
public interface CustomPrivilegeHandler {

    /**
     * 是否可以访问该服务
     * 
     * @param userId
     * @param actionName
     * @return
     */
    public boolean canAccessAction(long companyId, long userId, String actionName);
}
