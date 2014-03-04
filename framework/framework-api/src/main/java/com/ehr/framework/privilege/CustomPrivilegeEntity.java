package com.ehr.framework.privilege;

/**
 * 自定义权限
 *
 * @author zoe
 */
public final class CustomPrivilegeEntity {

    private final String name;
    private final String description;
    private final String parent;
    private final String type;
    private final boolean manager;
    private final int version;

    public CustomPrivilegeEntity(String name, String description, String parent, String type, boolean manager, int version) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.type = type;
        this.manager = manager;
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public String getParent() {
        return parent;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isManager() {
        return manager;
    }

    public int getVersion() {
        return version;
    }
}
