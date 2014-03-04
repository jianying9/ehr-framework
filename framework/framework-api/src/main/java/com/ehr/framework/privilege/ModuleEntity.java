package com.ehr.framework.privilege;

/**
 * 模块分类
 *
 * @author zoe
 */
public final class ModuleEntity {
    //模块分类名称

    private final String name;
    //描述
    private final String description;
    //上级模块，空字符表示一级模块分类
    private final String parentName;
    //伪实现
    private final boolean pseudo;
    //版本
    private final int versionType;
    //自定义权限
    private final String[] customPrivilegeName;
    //是否管理员权限
    private final boolean requireAdmin;

    public ModuleEntity(final String name, final String description, final String parentName, final boolean pseudo, final int versionType, final String[] customPrivilegeName, final boolean requireAdmin) {
        this.name = name;
        this.description = description;
        this.parentName = parentName;
        this.pseudo = pseudo;
        this.versionType = versionType;
        this.customPrivilegeName = customPrivilegeName;
        this.requireAdmin = requireAdmin;
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
