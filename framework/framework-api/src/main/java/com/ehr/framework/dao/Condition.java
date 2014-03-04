package com.ehr.framework.dao;

/**
 * 查询条件
 * @author zoe
 */
public final class Condition {

    private String columnName;
    private String tableAliasName;
    private String columnValue;
    private String keyWord;
    private boolean type;

    /**
     * 构造函数
     * @param columnName 列名
     * @param conditionTypeEnum 条件类型
     * @param columnValue 值
     */
    public Condition(final String columnName, final ConditionTypeEnum conditionTypeEnum, String columnValue) {
        this("", columnName, conditionTypeEnum, columnValue, true);
    }

    /**
     * 构造函数
     * @param columnName 列名
     * @param conditionTypeEnum 条件类型
     * @param columnValue 值
     * @param type 连接方式true-AND,false-OR
     */
    public Condition(final String columnName, final ConditionTypeEnum conditionTypeEnum, String columnValue, boolean type) {
        this("", columnName, conditionTypeEnum, columnValue, type);
    }

    public Condition(final String tableAliasName, final String columnName, final ConditionTypeEnum conditionTypeEnum, String columnValue, boolean type) {
        if (columnName == null || columnName.isEmpty()) {
            throw new RuntimeException("There was an error instancing Condition. Cause: columnName is null or empty.");
        }
        if (columnValue == null) {
            throw new RuntimeException("There was an error instancing Condition. Cause: columnValue is null.");
        }
        this.tableAliasName = tableAliasName;
        this.columnName = columnName;
        this.keyWord = conditionTypeEnum.getKeyWord();
        this.type = type;
        switch (conditionTypeEnum) {
            case LIKE:
                StringBuilder likeValueBuilder = new StringBuilder(columnValue.length() + 2);
                likeValueBuilder.append('%').append(columnValue).append('%');
                this.columnValue = likeValueBuilder.toString();
                break;
            case HEAD_AND_LIKE:
                this.columnValue = columnValue.concat("%");
                break;
            case END_AND_LIKE:
                this.columnValue = "%".concat(columnValue);
                break;
            case IN:
                StringBuilder inValueBuilder = new StringBuilder(columnValue.length() + 2);
                inValueBuilder.append('(').append(columnValue).append(')');
                this.columnValue = inValueBuilder.toString();
                break;
            default:
                this.columnValue = columnValue;
        }
    }

    public String getTableAliasName() {
        return this.tableAliasName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public String getColumnValue() {
        return this.columnValue;
    }

    public boolean isAnd() {
        return this.type;
    }
}
