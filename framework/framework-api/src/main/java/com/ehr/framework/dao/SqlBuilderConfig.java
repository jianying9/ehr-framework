package com.ehr.framework.dao;

/**
 * 负责创建SQL, 在调用该类的所有方法之前，请确保所有的参数不为null，集合参数不为empty
 * @author zoe
 */
public final class SqlBuilderConfig {

    private SqlBuilderConfig() {
    }
    //注意空格
    public static final String SELECT = "SELECT ";
    public static final String FROM = " FROM ";
    public static final String UPDATE = "UPDATE ";
    public static final String INSERT = "INSERT INTO ";
    public static final String DELETE = "DELETE FROM ";
    public static final String VALUES = " VALUES ";
    public static final String WHERE = " WHERE ";
    public static final String SET = " SET ";
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String IN = " IN ";
    public static final String LIKE = " LIKE ";
    public static final String EQUAL = "=";
    public static final String NOT_EQUAL = " <> ";
    public static final String GREATER = ">";
    public static final String LESS = "<";
    public static final String EQUAL_AND_GREATER = ">=";
    public static final String EQUAL_AND_LESS = "<=";
    public static final String ASC = " ASC";
    public static final String DESC = " DESC";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String LEFT = " LEFT ";
    public static final String JOIN = " JOIN ";
    public static final String ON = " ON ";
    public static final String COUNT = " COUNT(*) ";
    public static final String LIMIT = " LIMIT ";
}
