package com.ehr.framework.jdbc;

import com.ehr.framework.dao.Condition;
import com.ehr.framework.dao.ConditionTypeEnum;
import com.ehr.framework.dao.Order;
import com.ehr.framework.dao.SqlBuilderConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 负责创建SQL, 在调用该类的所有方法之前，请确保所有的参数不为null，集合参数不为empty
 * @author zoe
 */
public final class SqlBuilder {

    private SqlBuilder() {
    }

    /**
     * 生成查询语句模板
     * @param tableName
     * @param selectFields
     * @return 
     */
    public static String inquireSqlModelBuild(final String tableName, final String[] selectFields) {
        StringBuilder sqlBuilder = new StringBuilder(selectFields.length * 20);
        sqlBuilder.append(SqlBuilderConfig.SELECT);
        if (selectFields.length == 0) {
            sqlBuilder.append('*');
        } else {
            for (String field : selectFields) {
                sqlBuilder.append(field).append(",");
            }
            sqlBuilder.setLength(sqlBuilder.length() - 1);
        }
        sqlBuilder.append(SqlBuilderConfig.FROM).append(tableName);
        return sqlBuilder.toString();
    }

    /**
     * 生成视图查询模板
     * @param mainTable
     * @param relationalTableList
     * @param columnList
     * @return 
     */
    public static String inquireViewSqlModelBuild(final MainTable mainTable, final List<RelationalTable> relationalTableList, final List<Column> columnList) {
        StringBuilder sqlBuilder = new StringBuilder(columnList.size() * 20);
        sqlBuilder.append(SqlBuilderConfig.SELECT);
        for (Column column : columnList) {
            sqlBuilder.append(column.getTableAliasName()).append('.').append(column.getFieldName()).append(' ').append(column.getAliasName()).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        String mainAliasName = mainTable.getAliasName();
        sqlBuilder.append(SqlBuilderConfig.FROM).append(mainTable.getTableName()).append(' ').append(mainAliasName);
        String[] mainKeys;
        String[] keys;
        String relationalAliasName;
        for (RelationalTable relationalTable : relationalTableList) {
            if (!relationalTable.isInner()) {
                sqlBuilder.append(SqlBuilderConfig.LEFT);
            }
            sqlBuilder.append(SqlBuilderConfig.JOIN).append(relationalTable.getTableName()).append(' ').append(relationalTable.getAliasName()).append(SqlBuilderConfig.ON);
            mainKeys = relationalTable.getMainKeys();
            keys = relationalTable.getKeys();
            relationalAliasName = relationalTable.getAliasName();
            for (int index = 0; index < mainKeys.length && index < keys.length; index++) {
                sqlBuilder.append(mainAliasName).append('.').append(mainKeys[index]).append('=').append(relationalAliasName).append('.').append(keys[index]).append(SqlBuilderConfig.AND);
            }
            sqlBuilder.setLength(sqlBuilder.length() - SqlBuilderConfig.AND.length());
        }
        return sqlBuilder.toString();
    }

    /**
     * 生成视图统计SQL模板
     * @param mainTable
     * @param relationalTableList
     * @return 
     */
    public static String countViewSqlModelBuild(final MainTable mainTable, final List<RelationalTable> relationalTableList) {
        StringBuilder sqlBuilder = new StringBuilder(512);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(SqlBuilderConfig.COUNT);
        String mainAliasName = mainTable.getAliasName();
        sqlBuilder.append(SqlBuilderConfig.FROM).append(mainTable.getTableName()).append(' ').append(mainAliasName);
        String[] mainKeys;
        String[] keys;
        String relationalAliasName;
        for (RelationalTable relationalTable : relationalTableList) {
            if (!relationalTable.isInner()) {
                sqlBuilder.append(SqlBuilderConfig.LEFT);
            }
            sqlBuilder.append(SqlBuilderConfig.JOIN).append(relationalTable.getTableName()).append(' ').append(relationalTable.getAliasName()).append(SqlBuilderConfig.ON);
            mainKeys = relationalTable.getMainKeys();
            keys = relationalTable.getKeys();
            relationalAliasName = relationalTable.getAliasName();
            for (int index = 0; index < mainKeys.length && index < keys.length; index++) {
                sqlBuilder.append(mainAliasName).append('.').append(mainKeys[index]).append('=').append(relationalAliasName).append('.').append(keys[index]).append(SqlBuilderConfig.AND);
            }
            sqlBuilder.setLength(sqlBuilder.length() - SqlBuilderConfig.AND.length());
        }
        return sqlBuilder.toString();
    }

    /**
     * 生成视图统计SQL
     * @param countViewModel
     * @param conditionList
     * @return 
     */
    public static String countViewSqlBuild(final String countViewModel, final List<Condition> conditionList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + countViewModel.length() + 30);
        sqlBuilder.append(countViewModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        return sqlBuilder.toString();
    }

    /**
     * 构造单条件查询SQL
     * @param inquireSqlModel 查询语句模板Condition
     * @param conditionName 条件名
     * @return 
     */
    public static String inquireSqlBuild(final String inquireSqlModel, final String conditionName) {
        StringBuilder sqlBuilder = new StringBuilder(inquireSqlModel.length() + conditionName.length() + 9);
        sqlBuilder.append(inquireSqlModel).append(SqlBuilderConfig.WHERE).append(conditionName).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 构造单条件查询SQL
     * @param inquireSqlModel 查询语句模板
     * @param conditionName 条件名
     * @return 
     */
    public static String inquirePageSqlBuild(final String inquireSqlModel, final String conditionName, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(inquireSqlModel.length() + conditionName.length() + 19 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(inquireSqlModel).append(SqlBuilderConfig.WHERE).append(conditionName).append("=?").append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * 构造单条件查询主键SQL
     * @param tableName 表
     * @param keyField 主键
     * @param conditionName 条件
     * @return 
     */
    public static String inquireKeysSqlBuild(final String tableName, final String keyField, final String conditionName) {
        StringBuilder sqlBuilder = new StringBuilder(tableName.length() + keyField.length() + conditionName.length() + 22);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE).append(conditionName).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 构造单条件分页查询主键SQL
     * @param tableName
     * @param keyField
     * @param conditionName
     * @return 
     */
    public static String inquirePageKeysSqlBuild(final String tableName, final String keyField, final String conditionName, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(tableName.length() + keyField.length() + conditionName.length() + 32 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE).append(conditionName).append("=?").append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * 构造主键in查询
     * @param inquireSqlModel 查询语句模板
     * @param keyField 主键名
     * @param valueCollection 键值集合
     * @return 
     */
    public static String inquireByKeyCollectionSqlBuild(final String inquireSqlModel, final String keyField, final Collection<Long> values) {
        StringBuilder sqlBuilder = new StringBuilder(values.size() * 15 + inquireSqlModel.length());
        sqlBuilder.append(inquireSqlModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE).append(keyField).append(SqlBuilderConfig.IN).append('(');
        for (long value : values) {
            sqlBuilder.append(Long.toString(value)).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        sqlBuilder.append(')');
        return sqlBuilder.toString();
    }

    /**
     * 构造双条件查询主键SQL
     * @param tableName 表
     * @param keyField 主键名
     * @param columnName 条件名1
     * @param columnNameTwo 条件名2
     * @return 
     */
    public static String inquireKeysSqlBuild(final String tableName, final String keyField, final String columnName, final String columnNameTwo) {
        StringBuilder sqlBuilder = new StringBuilder(tableName.length() + keyField.length() + columnName.length() + columnNameTwo.length() + 29);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE).append(columnName).append("=?").append(SqlBuilderConfig.AND).append(columnNameTwo).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 构造双条件查询SQL
     * @param inquireSqlModel 查询语句模板
     * @param columnName 条件名1
     * @param columnNameTwo 条件名2
     * @return 
     */
    public static String inquireSqlBuild(final String inquireSqlModel, final String columnName, final String columnNameTwo) {
        StringBuilder sqlBuilder = new StringBuilder(inquireSqlModel.length() + columnName.length() + columnNameTwo.length() + 16);
        sqlBuilder.append(inquireSqlModel).append(SqlBuilderConfig.WHERE).append(columnName).append("=?").append(SqlBuilderConfig.AND).append(columnNameTwo).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 多条件查询主键SQL
     * @param tableName
     * @param keyField
     * @param conditionList
     * @return 
     */
    public static String inquireKeysSqlBuild(final String tableName, final String keyField, final List<Condition> conditionList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + tableName.length() + keyField.length() + 20);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        return sqlBuilder.toString();
    }

    /**
     * 多条件分页查询主键SQL
     * @param tableName
     * @param keyField
     * @param conditionList
     * @return 
     */
    public static String inquirePageKeysSqlBuild(final String tableName, final String keyField, final List<Condition> conditionList, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + tableName.length() + keyField.length() + 30 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        sqlBuilder.append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * condition build
     * @param sqlBuilder
     * @param conditionList 
     */
    private static void conditionBuild(StringBuilder sqlBuilder, List<Condition> conditionList) {
        List<Condition> orConditionList = new ArrayList<Condition>(conditionList.size());
        String tableAliasName;
        for (Condition condition : conditionList) {
            if (condition.isAnd()) {
                tableAliasName = condition.getTableAliasName();
                if (!tableAliasName.isEmpty()) {
                    sqlBuilder.append(tableAliasName).append('.');
                }
                sqlBuilder.append(condition.getColumnName()).append(condition.getKeyWord());
                if (condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    sqlBuilder.append(condition.getColumnValue());
                } else {
                    sqlBuilder.append('?');
                }
                sqlBuilder.append(SqlBuilderConfig.AND);
            } else {
                orConditionList.add(condition);
            }
        }
        //or条件
        if (!orConditionList.isEmpty()) {
            sqlBuilder.append('(');
            for (Condition condition : orConditionList) {
                tableAliasName = condition.getTableAliasName();
                if (!tableAliasName.isEmpty()) {
                    sqlBuilder.append(tableAliasName).append('.');
                }
                sqlBuilder.append(condition.getColumnName()).append(condition.getKeyWord());
                if (condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    sqlBuilder.append(condition.getColumnValue());
                } else {
                    sqlBuilder.append('?');
                }
                sqlBuilder.append(SqlBuilderConfig.OR);
            }
            sqlBuilder.setLength(sqlBuilder.length() - SqlBuilderConfig.OR.length());
            sqlBuilder.append(')');
        }
        if (!conditionList.isEmpty() && orConditionList.isEmpty()) {
            sqlBuilder.setLength(sqlBuilder.length() - SqlBuilderConfig.AND.length());
        }
    }

    /**
     * order build
     * @param sqlBuilder
     * @param orderList 
     */
    private static void orderBulid(StringBuilder sqlBuilder, List<Order> orderList) {
        String tableAliasName;
        for (Order order : orderList) {
            tableAliasName = order.getTableAliasName();
            if (!tableAliasName.isEmpty()) {
                sqlBuilder.append(tableAliasName).append('.');
            }
            sqlBuilder.append(order.getColumnName()).append(order.getKeyWord()).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
    }

    /**
     * 获取条件值
     * @param conditionList
     * @return 
     */
    public static String[] getConditionValues(List<Condition> conditionList) {
        List<String> valueList = new ArrayList<String>(conditionList.size());
        List<Condition> orConditionList = new ArrayList<Condition>(conditionList.size());
        for (Condition condition : conditionList) {
            if (condition.isAnd()) {
                if (!condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    valueList.add(condition.getColumnValue());
                }
            } else {
                orConditionList.add(condition);
            }
        }
        //or条件
        if (!orConditionList.isEmpty()) {
            for (Condition condition : orConditionList) {
                if (!condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    valueList.add(condition.getColumnValue());
                }
            }
        }
        String[] values = valueList.toArray(new String[valueList.size()]);
        return values;
    }

    /**
     * 多条件查询SQL
     * @param inquireSqlModel 查询模板
     * @param conditionList 条件集合
     * @return 
     */
    public static String conditionSqlBuild(final String inquireSqlModel, final List<Condition> conditionList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + inquireSqlModel.length() + 17);
        sqlBuilder.append(inquireSqlModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        return sqlBuilder.toString();
    }

    /**
     * 多条件分页查询SQL
     * @param inquireSqlModel
     * @param conditionList
     * @return 
     */
    public static String pageConditionSqlBuild(final String inquireSqlModel, final List<Condition> conditionList, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + inquireSqlModel.length() + 17 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(inquireSqlModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        sqlBuilder.append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * 多条件查询主键SQL，带排序条件
     * @param tableName 表名
     * @param keyField 主键名
     * @param conditionList 条件集合
     * @param orderList 排序集合
     * @return 
     */
    public static String inquireKeysSqlBuild(final String tableName, final String keyField, final List<Condition> conditionList, final List<Order> orderList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 20 + tableName.length() + keyField.length() + 30);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        if (!orderList.isEmpty()) {
            sqlBuilder.append(SqlBuilderConfig.ORDER_BY);
            SqlBuilder.orderBulid(sqlBuilder, orderList);
        }
        return sqlBuilder.toString();
    }

    /**
     * 多条件分页查询主键SQL，带排序条件
     * @param tableName
     * @param keyField
     * @param conditionList
     * @param orderList
     * @return 
     */
    public static String inquirePageKeysSqlBuild(final String tableName, final String keyField, final List<Condition> conditionList, final List<Order> orderList, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 20 + tableName.length() + keyField.length() + 40 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(keyField).append(SqlBuilderConfig.FROM).append(tableName);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        if (!orderList.isEmpty()) {
            sqlBuilder.append(SqlBuilderConfig.ORDER_BY);
            SqlBuilder.orderBulid(sqlBuilder, orderList);
        }
        sqlBuilder.append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * 多条件查询SQL，带排序条件
     * @param inquireSqlModel 查询语句模板
     * @param conditionList 查询条件集合
     * @param orderList 排序条件集合
     * @return 
     */
    public static String conditionSqlBuild(final String inquireSqlModel, final List<Condition> conditionList, final List<Order> orderList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + inquireSqlModel.length());
        sqlBuilder.append(inquireSqlModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        if (!orderList.isEmpty()) {
            sqlBuilder.append(SqlBuilderConfig.ORDER_BY);
            SqlBuilder.orderBulid(sqlBuilder, orderList);
        }
        return sqlBuilder.toString();
    }

    /**
     * 多条件查询SQL，带排序条件
     * @param inquireSqlModel 查询语句模板
     * @param conditionList 查询条件集合
     * @param orderList 排序条件集合
     * @return 
     */
    public static String pageConditionSqlBuild(final String inquireSqlModel, final List<Condition> conditionList, final List<Order> orderList, int startIndex, int rows) {
        String startIndexStr = Integer.toString(startIndex);
        String rowsStr = Integer.toString(rows);
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + inquireSqlModel.length() + orderList.size() * 10 + 27 + startIndexStr.length() + rowsStr.length());
        sqlBuilder.append(inquireSqlModel);
        sqlBuilder.append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        if (!orderList.isEmpty()) {
            sqlBuilder.append(SqlBuilderConfig.ORDER_BY);
            SqlBuilder.orderBulid(sqlBuilder, orderList);
        }
        sqlBuilder.append(SqlBuilderConfig.LIMIT).append(startIndexStr).append(',').append(rowsStr);
        return sqlBuilder.toString();
    }

    /**
     * 构造插入语句
     * @param tableName 表名
     * @param insertFields 插入列集合
     * @return 
     */
    public static String insertSqlBuild(final String tableName, final String[] insertFields) {
        StringBuilder sqlBuilder = new StringBuilder(insertFields.length * 20);
        sqlBuilder.append(SqlBuilderConfig.INSERT).append(tableName).append(" (");
        for (String field : insertFields) {
            sqlBuilder.append(field).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        sqlBuilder.append(')').append(SqlBuilderConfig.VALUES).append('(');
        for (int index = 0; index < insertFields.length; index++) {
            sqlBuilder.append("?,");
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        sqlBuilder.append(')');
        return sqlBuilder.toString();
    }

    /**
     * 构造删除语句
     * @param tableName 表名
     * @param keyField 主键名
     * @return 
     */
    public static String deleteSqlBuild(final String tableName, final String keyField) {
        StringBuilder sqlBuilder = new StringBuilder(21 + tableName.length() + keyField.length());
        sqlBuilder.append(SqlBuilderConfig.DELETE).append(tableName).append(SqlBuilderConfig.WHERE).append(keyField).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 构造删除所有数据语句
     * @param tableName 表名
     * @param keyField 主键名
     * @return 
     */
    @Deprecated
    public static String deleteAllSqlBuild(final String tableName, final String keyField) {
        StringBuilder sqlBuilder = new StringBuilder(22 + tableName.length() + keyField.length());
        sqlBuilder.append(SqlBuilderConfig.DELETE).append(tableName).append(SqlBuilderConfig.WHERE).append(keyField).append(">-1");
        return sqlBuilder.toString();
    }

    /**
     * 构造动态更新语句
     * @param tableName 表名
     * @param updateFields 可更新列集合
     * @param entityMap 更新值
     * @param keyFiled 主键名
     * @return 
     */
    public static String updataSqlBuild(final String tableName, final String[] updateFields, final Map<String, String> entityMap, final String keyField) {
        StringBuilder sqlBuilder = new StringBuilder(updateFields.length * 20);
        sqlBuilder.append(SqlBuilderConfig.UPDATE).append(tableName).append(SqlBuilderConfig.SET);
        for (String field : updateFields) {
            if (entityMap.containsKey(field)) {
                sqlBuilder.append(field).append("=?,");
            }
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        sqlBuilder.append(SqlBuilderConfig.WHERE).append(keyField).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 打印单行值
     * @param values 值
     * @return 
     */
    public static String valuesBuild(String[] values) {
        String result = "";
        if (values.length > 0) {
            StringBuilder valueBuilder = new StringBuilder(values.length * 20);
            for (String value : values) {
                valueBuilder.append(value).append(',');
            }
            valueBuilder.setLength(valueBuilder.length() - 1);
            result = valueBuilder.toString();
        }
        return result;
    }

    /**
     * 打印多行值
     * @param valueArrays
     * @return 
     */
    public static String valuesBuild(String[][] valueArrays) {
        String result = "";
        if (valueArrays.length > 0) {
            StringBuilder valueBuilder = new StringBuilder(valueArrays.length * 200);
            for (String[] values : valueArrays) {
                if (values.length > 0) {
                    for (String value : values) {
                        valueBuilder.append(value).append(',');
                    }
                    valueBuilder.setLength(valueBuilder.length() - 1);
                }
                valueBuilder.append('\n');
            }
            valueBuilder.setLength(valueBuilder.length() - 1);
            result = valueBuilder.toString();
        }
        return result;
    }

    /**
     * 将值用逗号连接
     * @param values 值
     * @return 
     */
    public static String collectionBuild(Collection<Long> values) {
        String result = "";
        if (!values.isEmpty()) {
            StringBuilder valueBuilder = new StringBuilder(values.size() * 20);
            for (long value : values) {
                valueBuilder.append(value).append(',');
            }
            valueBuilder.setLength(valueBuilder.length() - 1);
            result = valueBuilder.toString();
        }
        return result;
    }

    /**
     * 构造单条件查询记录总数SQL
     * @param tableName
     * @param conditionName
     * @return 
     */
    public static String countSqlBuild(final String tableName, final String conditionName) {
        StringBuilder sqlBuilder = new StringBuilder(tableName.length() + conditionName.length() + 32);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(SqlBuilderConfig.COUNT).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE).append(conditionName).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 构造双条件查询记录总数SQL
     * @param tableName
     * @param columnName
     * @param columnNameTwo
     * @return 
     */
    public static String countSqlBuild(final String tableName, final String columnName, final String columnNameTwo) {
        StringBuilder sqlBuilder = new StringBuilder(tableName.length() + columnName.length() + columnNameTwo.length() + 39);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(SqlBuilderConfig.COUNT).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE).append(columnName).append("=?").append(SqlBuilderConfig.AND).append(columnNameTwo).append("=?");
        return sqlBuilder.toString();
    }

    /**
     * 多条件查询主键SQL
     * @param tableName
     * @param keyField
     * @param conditionList
     * @return 
     */
    public static String countSqlBuild(final String tableName, final List<Condition> conditionList) {
        StringBuilder sqlBuilder = new StringBuilder(conditionList.size() * 15 + tableName.length() + 30);
        sqlBuilder.append(SqlBuilderConfig.SELECT).append(SqlBuilderConfig.COUNT).append(SqlBuilderConfig.FROM).append(tableName).append(SqlBuilderConfig.WHERE);
        SqlBuilder.conditionBuild(sqlBuilder, conditionList);
        return sqlBuilder.toString();
    }

    /**
     * 解析成like值
     * @param filedValue
     * @return
     */
    public static String parseLikeValue(String value) {
        StringBuilder builder = new StringBuilder(value.length() + 2);
        builder.append('%').append(value).append('%');
        return builder.toString();
    }
}
