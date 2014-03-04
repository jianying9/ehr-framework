package com.ehr.framework.jdbc;

/**
 *
 * @author zoe
 */
public class RelationalTable {
    
    private String tableName;
    private String aliasName;
    private String[] mainKeys;
    private String[] keys;
    private boolean inner;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getMainKeys() {
        return mainKeys;
    }

    public void setMainKeys(String[] mainKeys) {
        this.mainKeys = mainKeys;
    }

    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean outer) {
        this.inner = outer;
    }
}
