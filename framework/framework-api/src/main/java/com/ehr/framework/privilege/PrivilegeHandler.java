package com.ehr.framework.privilege;

/**
 * 权限控制
 * @author zoe
 */
public interface PrivilegeHandler {

    boolean hasPrivilege(String actionName);
}
