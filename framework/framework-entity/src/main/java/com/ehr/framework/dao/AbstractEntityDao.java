package com.ehr.framework.dao;

import com.ehr.framework.jdbc.SqlBuilder;
import com.ehr.framework.jdbc.SqlExecuter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * entity dao
 * @author zoe
 */
public abstract class AbstractEntityDao<T extends Entity> extends AbstractDao<T> {

    //inquireByKeySql
    protected String inquireByKeySql;
    //insertSql
    protected String insertSql;
    //deleteSql
    protected String deleteSql;

    AbstractEntityDao() {
    }

    protected final void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    protected final void setInquireByKeySql(String inquireByKeySql) {
        this.inquireByKeySql = inquireByKeySql;
    }

    protected final void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    /**
     * 使用该功能前确保mysql数据引擎InnoDB
     * 针对该记录添加一数据库同步锁。适用用多应用服务器同数据库结构线程同步
     * 利用InnoDB行锁，通过一条无变化的更新语句实现同步
     * @param keyValue 
     */
    public final void synchronize(long keyValue) {
        StringBuilder sqlBuilder = new StringBuilder(this.keyField.length() * 2 + this.tableName.length() + 24);
        sqlBuilder.append(SqlBuilderConfig.UPDATE).append(this.tableName).append(SqlBuilderConfig.SET);
        sqlBuilder.append(this.keyField).append("=?");
        sqlBuilder.append(SqlBuilderConfig.WHERE).append(this.keyField).append("=?");
        String key = Long.toString(keyValue);
        String[] values = {key, key};
        String updateSql = sqlBuilder.toString();
        SqlExecuter.update(this.dataSource, updateSql, values);
    }

    /**
     * 根据主键查询,无缓存
     * @param keyValue
     * @return 
     */
    public final T inquireByKeyNoCache(final long keyValue) {
        T t = null;
        Map<String, String> resultMap = SqlExecuter.inquireByKey(this.dataSource, this.inquireByKeySql, keyValue);
        if (resultMap != null) {
            t = this.newInstance(resultMap);
        }
        return t;
    }

    /**
     * 根据主键集合查询，无缓存
     * @param values
     * @return 
     */
    public final List<T> inquireByKeysNoCache(final Collection<Long> values) {
        List<T> tList;
        if (values.isEmpty()) {
            tList = new ArrayList<T>(0);
        } else {
            String inquireByKeysSql = SqlBuilder.inquireByKeyCollectionSqlBuild(this.inquireSqlModel, this.keyField, values);
            List<Map<String, String>> resultMapList = SqlExecuter.inquire(this.dataSource, inquireByKeysSql);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 单个条件动态查询，无缓存
     * @param columnName 定义列名
     * @param columnValue 值
     * @return 
     */
    public final List<T> inquireByColumnNoCache(final String columnName, final String columnValue) {
        String inquireSql = SqlBuilder.inquireSqlBuild(this.inquireSqlModel, columnName);
        List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, columnValue);
        return this.parseMapList(resultMapList);
    }

    /**
     * 单个条件动态分页查询，无缓存
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<T> inquirePageByColumnNoCache(String columnName, String columnValue, int startIndex, int rows) {
        String inquirePageSql = SqlBuilder.inquirePageSqlBuild(this.inquireSqlModel, columnName, startIndex, rows);
        List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquirePageSql, columnValue);
        return this.parseMapList(resultMapList);
    }

    /**
     * 两个条件动态查询,无缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public final List<T> inquireByColumnsNoCache(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo) {
        String inquireSql = SqlBuilder.inquireSqlBuild(this.inquireSqlModel, columnName, columnNameTwo);
        String[] values = {columnValue, columnValueTwo};
        List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
        return this.parseMapList(resultMapList);
    }

    /**
     * 多条件动态查询,无缓存
     * @param conditionList 条件集合
     * @return 
     */
    public final List<T> inquireByConditionNoCache(final List<Condition> conditionList) {
        List<T> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<T>(0);
        } else {
            String inquireSql = SqlBuilder.conditionSqlBuild(this.inquireSqlModel, conditionList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件动态分页查询,无缓存
     * @param conditionList
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<T> inquirePageByConditionNoCache(List<Condition> conditionList, int startIndex, int rows) {
        List<T> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<T>(0);
        } else {
            String inquireSql = SqlBuilder.pageConditionSqlBuild(this.inquireSqlModel, conditionList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件动态查询，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public final List<T> inquireByConditionNoCache(final List<Condition> conditionList, final List<Order> orderList) {
        List<T> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<T>(0);
        } else {
            String inquireSql = SqlBuilder.conditionSqlBuild(this.inquireSqlModel, conditionList, orderList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件动态分页查询，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<T> inquirePageByConditionNoCache(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        List<T> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<T>(0);
        } else {
            String inquireSql = SqlBuilder.pageConditionSqlBuild(this.inquireSqlModel, conditionList, orderList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 插入
     * @param entityMap 
     */
    protected final void insertNoCache(final Map<String, String> entityMap) {
        String[] values = this.getInsertValues(entityMap);
        SqlExecuter.insert(this.dataSource, this.insertSql, values);
    }

    /**
     * 批量插入
     * @param entityMapList 
     */
    protected final void batchInsertNoCache(final List<Map<String, String>> entityMapList) {
        String[][] valueArrays = new String[entityMapList.size()][];
        Map<String, String> entityMap;
        for (int index = 0; index < entityMapList.size(); index++) {
            entityMap = entityMapList.get(index);
            valueArrays[index] = this.getInsertValues(entityMap);
        }
        SqlExecuter.batchInsert(dataSource, insertSql, valueArrays);
    }

    /**
     * 插入并查询
     * @param entityMap
     * @return 
     */
    protected final T insertAndInquireNoCache(final Map<String, String> entityMap) {
        String[] values = this.getInsertValues(entityMap);
        Map<String, String> resultMap = SqlExecuter.insertAndInquireByKey(this.dataSource, this.insertSql, values, this.inquireByKeySql);
        final T t = this.newInstance(resultMap);
        return t;
    }

    /**
     * 更新,并移除相应缓存
     * @param entityMap 
     */
    protected final void updateNoCache(final Map<String, String> entityMap) {
        if (this.assertCanUpdate(entityMap)) {
            String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
            String[] values = this.getUpdateValues(entityMap);
            SqlExecuter.update(this.dataSource, updateSql, values);
        }
    }

    /**
     * 批量的更新
     * @param entityMapList 
     */
    protected final void batchUpdateNoCache(final List<Map<String, String>> entityMapList) {
        List<Map<String, String>> validEntityMapList = new ArrayList<Map<String, String>>(entityMapList.size());
        Map<String, String> entityMap;
        for (int index = 0; index < entityMapList.size(); index++) {
            entityMap = entityMapList.get(index);
            if (this.assertCanUpdate(entityMap)) {
                validEntityMapList.add(entityMap);
            }
        }
        if (!validEntityMapList.isEmpty()) {
            entityMap = validEntityMapList.get(0);
            String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
            String[][] valueArrays = new String[entityMapList.size()][];
            for (int index = 0; index < validEntityMapList.size(); index++) {
                entityMap = entityMapList.get(index);
                valueArrays[index] = this.getUpdateValues(entityMap);
            }
            SqlExecuter.batchUpdate(this.dataSource, updateSql, valueArrays);
        }
    }

    /**
     * 更新并查询后新后值
     * @param entityMap
     * @return insertFields
     */
    protected final T updateAndInquireNoCache(final Map<String, String> entityMap) {
        T t = null;
        if (this.assertCanUpdate(entityMap)) {
            String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
            String[] values = this.getUpdateValues(entityMap);
            Map<String, String> resultMap = SqlExecuter.updateAndInquireByKey(this.dataSource, updateSql, values, this.inquireByKeySql);
            if (resultMap != null) {
                t = this.newInstance(resultMap);
            }
        }
        return t;
    }

    /**String[] values = valueList.toArray(new String[valueList.size()]);
     * 删除,移除缓存
     * @param keyValue 
     */
    protected final void deleteNoCache(long keyValue) {
        SqlExecuter.delete(this.dataSource, this.deleteSql, keyValue);
    }

    /**
     * 批量删除
     * @param keyValues 
     */
    protected final void batchDeleteNoCache(Collection<Long> keyValues) {
        SqlExecuter.batchDelete(this.dataSource, this.deleteSql, keyValues);
    }

    /**
     * 单个条件动态查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public final List<Long> inquireKeysByColumnNoCache(final String columnName, final String columnValue) {
        String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, columnName);
        String[] values = {columnValue};
        return SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
    }

    /**
     * 单个条件动态分页查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<Long> inquirePageKeysByColumnNoCache(String columnName, String columnValue, int startIndex, int rows) {
        String inquirePageKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, columnName, startIndex, rows);
        return SqlExecuter.inquireKeys(this.dataSource, inquirePageKeysSql, columnValue);
    }

    /**
     * 两个条件动态查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return 
     */
    public final List<Long> inquireKeysByColumnsNoCache(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, columnName, columnNameTwo);
        String[] values = {columnValue, columnValueTwo};
        return SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
    }

    /**
     * 多个条件动态查询主键集合，无缓存
     * @param conditionList
     * @return 
     */
    public final List<Long> inquireKeysByConditionNoCache(List<Condition> conditionList) {
        List<Long> keyList;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            final String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, conditionList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 多个条件动态分页查询主键集合，无缓存
     * @param conditionList
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<Long> inquirePageKeysByConditionNoCache(List<Condition> conditionList, int startIndex, int rows) {
        List<Long> keyList;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            String inquireKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, conditionList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 多个条件动态查询主键集合，可排序，无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public final List<Long> inquireKeysByConditionNoCache(List<Condition> conditionList, List<Order> orderList) {
        List<Long> keyList;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, conditionList, orderList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 多个条件动态分页查询主键集合，可排序，无缓存
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return 
     */
    public final List<Long> inquirePageKeysByConditionNoCache(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        List<Long> keyList;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            String inquireKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, conditionList, orderList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 单个条件动态查询总记录数，无缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public final int countByColumnNoCache(String columnName, String columnValue) {
        String countSql = SqlBuilder.countSqlBuild(this.tableName, columnName);
        String[] values = {columnValue};
        return SqlExecuter.count(this.dataSource, countSql, values);
    }

    /**
     * 两个条件动态查询总记录数，无缓存
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return 
     */
    public final int countByColumnsNoCache(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        String countSql = SqlBuilder.countSqlBuild(this.tableName, columnName, columnNameTwo);
        String[] values = {columnValue, columnValueTwo};
        return SqlExecuter.count(this.dataSource, countSql, values);
    }

    /**
     * 多个条件动态查询总记录数，无缓存
     * @param conditionList
     * @return 
     */
    public final int countByConditionNoCache(List<Condition> conditionList) {
        int count = 0;
        if (!conditionList.isEmpty()) {
            String countSql = SqlBuilder.countSqlBuild(this.tableName, conditionList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            count = SqlExecuter.count(this.dataSource, countSql, values);
        }
        return count;
    }
}
