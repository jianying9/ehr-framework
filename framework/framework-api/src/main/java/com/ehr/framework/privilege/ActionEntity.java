package com.ehr.framework.privilege;

/**
 *
 * @author zoe
 */
public final class ActionEntity {

    //服务名称
    private final String name;
    //描述
    private final String shortDescription;
    //模块名称
    private final String moduleName;
    //伪实现
    private final boolean pseudo;
    //版本
    private final int versionType;
    //自定义权限
    private final String[] customPrivilegeName;
    //是否需要管理员权限
    private final boolean requireAdmin;

    public ActionEntity(final String name, final String shortDescription, final String moduleName, final boolean pseudo, final int versionType, final String[] customPrivilegeName, final boolean requireAdmin) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.moduleName = moduleName;
        this.pseudo = pseudo;
        this.versionType = versionType;
        this.customPrivilegeName = customPrivilegeName;
        this.requireAdmin = requireAdmin;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public boolean isPseudo() {
        return pseudo;
    }

    public int getVersionType() {
        return versionType;
    }

    public String[] getCustomPrivilegeName() {
        return customPrivilegeName;
    }

    public boolean isRequireAdmin() {
        return requireAdmin;
    }
}
