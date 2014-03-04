package com.ehr.framework.privilege;

/**
 * 导航
 *
 * @author zoe
 */
public final class NavigationEntity {

    //菜单名称
    private final String name;
    //描述
    private final String description;
    //上级菜单，空字符表示一级菜单
    private final String parentName;
    //版本
    private final int versionType;
    //是否需要管理员
    private final boolean requireAdmin;
    //是否需要负责人
    private final boolean manager;
    //自定义权限
    private final String[] customPrivilegeName;

    public NavigationEntity(final String name, final String description, final String parentName, final int versionType, final boolean requireAdmin, final String[] customPrivilegeName, final boolean manager) {
        this.name = name;
        this.description = description;
        this.parentName = parentName;
        this.versionType = versionType;
        this.requireAdmin = requireAdmin;
        this.customPrivilegeName = customPrivilegeName;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public String getDescription() {
        return description;
    }

    public int getVersionType() {
        return versionType;
    }

    public boolean isRequireAdmin() {
        return requireAdmin;
    }

    public String[] getCustomPrivilegeName() {
        return customPrivilegeName;
    }

    public boolean isManager() {
        return manager;
    }
}
