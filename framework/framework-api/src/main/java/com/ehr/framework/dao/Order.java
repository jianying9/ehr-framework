package com.ehr.framework.dao;

/**
 * 顺序
 * @author zoe
 */
public final class Order {

    private String tableAliasName;
    private String columnName;
    private String keyWord;
    
    public Order(final String tableAliasName, final String columnName, final OrderTypeEnum orderTypeEnum) {
        this.columnName = columnName;
        this.keyWord = orderTypeEnum.getKeyWord();
        this.tableAliasName = tableAliasName;
    }

    public Order(final String columnName, final OrderTypeEnum orderTypeEnum) {
        this("", columnName, orderTypeEnum);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }
}
