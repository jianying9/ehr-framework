package com.ehr.framework.jdbc;

/**
 *
 * @author zoe
 */
public final class Column {

    private String fieldName;
    private String tableAliasName;
    private String aliasName;

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }
}
