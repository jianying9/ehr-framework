package com.ehr.framework.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * entity dao
 * @author zoe
 */
public interface EntityDao<T extends Entity> {

    /**
     * 使用该功能前确保mysql数据引擎InnoDB
     * 针对该记录添加一数据库同步锁。适用用多应用服务器同数据库结构线程同步
     * @param keyValue 
     */
    public void synchronize(long keyValue);

    /**
     * 根据主键查询,如果有缓存则使用缓存
     * @param key
     * @return 
     */
    public T inquireByKey(final long keyValue);

    /**
     * 根据主键查询,无缓存
     * @param keyValue
     * @return 
     */
    public T inquireByKeyNoCache(final long keyValue);

    /**
     * 根据主键集合查询，如果有缓存则使用缓存
     * @param keyValues
     * @return 
     */
    public List<T> inquireByKeys(final Collection<Long> keyValues);

    /**
     * 根据主键集合查询，无缓存
     * @param values
     * @return 
     */
    public List<T> inquireByKeysNoCache(final Collection<Long> values);

    /**
     * 单个条件查询，如果有缓存则使用缓存
     * @param columnName 列名
     * @param columnValue 值
     * @return 
     */
    public List<T> inquireByColumn(final String columnName, final String columnValue);

    /**
     * 单个条件分页查询，如果有缓存则使用缓存
     * @param columnName 列名
     * @param columnValue 值
     * @param startIndex 起始索引
     * @param rows 最大返回记录行数
     * @return 
     */
    public List<T> inquirePageByColumn(final String columnName, final String columnValue, final int startIndex, final int rows);

    /**
     * 单个条件查询，无缓存
     * @param columnName 列名
     * @param columnValue 值
     * @return 
     */
    public List<T> inquireByColumnNoCache(final String columnName, final String columnValue);

    /**
     * 单个条件分页查询，无缓存
     * @param columnName 列名
     * @param columnValue 值
     * @param startIndex 起始索引
     * @param rows 最大返回记录行数
     * @return 
     */
    public List<T> inquirePageByColumnNoCache(final String columnName, final String columnValue, final int startIndex, final int rows);

    /**
     * 两个条件查询,如果有缓存则使用缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public List<T> inquireByColumns(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 两个条件动态查询,无缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public List<T> inquireByColumnsNoCache(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 多条件动态查询,如果有缓存则使用缓存
     * @param conditionList 动态条件集合
     * @return 
     */
    public List<T> inquireByCondition(final List<Condition> conditionList);

    /**
     * 多条件动态分页查询,如果有缓存则使用缓存
     * @param conditionListfinal 动态条件集合
     * @param startIndex 起始索引
     * @param rows 最大返回记录行数
     * @return 
     */
    public List<T> inquirePageByCondition(final List<Condition> conditionListfinal, final int startIndex, final int rows);

    /**
     * 多条件动态查询,无缓存
     * @param conditionList 条件集合
     * @return 
     */
    public List<T> inquireByConditionNoCache(final List<Condition> conditionList);

    /**
     * 多条件动态分页查询,无缓存
     * @param conditionListfinal 动态条件集合
     * @param startIndex 起始索引
     * @param rows 最大返回记录行数
     * @return 
     */
    public List<T> inquirePageByConditionNoCache(final List<Condition> conditionList, final int startIndex, final int rows);

    /**
     * 多条件动态查询，带排序条件,如果有缓存则使用缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<T> inquireByCondition(final List<Condition> conditionList, final List<Order> orderList);

    /**
     * 多条件动态分页查询，带排序条件,如果有缓存则使用缓存
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return 
     */
    public List<T> inquirePageByCondition(final List<Condition> conditionList, final List<Order> orderList, final int startIndex, final int rows);

    /**
     * 多条件动态查询，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<T> inquireByConditionNoCache(final List<Condition> conditionList, final List<Order> orderList);

    /**
     * 多条件动态分页查询，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<T> inquirePageByConditionNoCache(final List<Condition> conditionList, final List<Order> orderList, final int startIndex, final int rows);

    /**
     * 插入，如果有缓存则同步缓存
     * @param entityMap 
     */
    public void insert(final Map<String, String> entityMap);

    /**
     * 批量插入，无缓存
     * @param entityMapList 
     */
    public void batchInsert(final List<Map<String, String>> entityMapList);

    /**
     * 插入并查询，如果有缓存则同步缓存
     * @param entityMap
     * @return 
     */
    public T insertAndInquire(final Map<String, String> entityMap);

    /**
     * 更新,如果有缓存则同步缓存
     * @param entityMap 
     */
    public void update(final Map<String, String> entityMap);

    /**
     * 批量更新,如果有缓存则同步缓存
     * @param entityMapList 
     */
    public void batchUpdate(final List<Map<String, String>> entityMapList);

    /**
     * 更新并查询后新后值，如果有缓存则同步缓存
     * @param entityMap
     * @return 
     */
    public T updateAndInquire(Map<String, String> entityMap);

    /**
     * 删除,如果有缓存则移除缓存
     * @param keyValue 
     */
    public void delete(long keyValue);

    /**
     * 批量删除,如果有缓存则移除缓存
     * @param keyValues 
     */
    public void batchDelete(final Collection<Long> keyValues);

    /**
     * 单个条件动态查询主键集合，如果有缓存则使用缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public List<Long> inquireKeysByColumn(final String columnName, final String columnValue);

    /**
     * 单个条件动态分页查询主键集合，如果有缓存则使用缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public List<Long> inquirePageKeysByColumn(final String columnName, final String columnValue, final int startIndex, final int rows);

    /**
     * 单个条件动态分页查询主键集合，无缓存
     * @param columnName 定义列名K
     * @param columnValue 值
     * @return 
     */
    public List<Long> inquireKeysByColumnNoCache(final String columnName, final String columnValue);

    /**
     * 单个条件动态分页查询主键集合，无缓存
     * @param columnName 定义列名K
     * @param columnValue 值
     * @return 
     */
    public List<Long> inquirePageKeysByColumnNoCache(final String columnName, final String columnValue, final int startIndex, final int rows);

    /**
     * 两个条件动态查询主键集合,如果有缓存则使用缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public List<Long> inquireKeysByColumns(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 两个条件动态查询主键集合,无缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public List<Long> inquireKeysByColumnsNoCache(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 多条件动态查询主键集合,如果有缓存则使用缓存
     * @param conditionList
     * @return 
     */
    public List<Long> inquireKeysByCondition(final List<Condition> conditionList);

    /**
     * 多条件动态分页查询主键集合,如果有缓存则使用缓存
     * @param conditionList
     * @param startIndex
     * @param rows
     * @return 
     */
    public List<Long> inquirePageKeysByCondition(final List<Condition> conditionList, final int startIndex, final int rows);

    /**
     * 多条件动态查询主键集合,无缓存
     * @param conditionList 条件集合
     * @return 
     */
    public List<Long> inquireKeysByConditionNoCache(final List<Condition> conditionList);

    /**
     * 多条件动态分页查询主键集合,无缓存
     * @param conditionList 条件集合
     * @return 
     */
    public List<Long> inquirePageKeysByConditionNoCache(final List<Condition> conditionList, final int startIndex, final int rows);

    /**
     * 多条件动态查询主键集合，带排序条件,如果有缓存则使用缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<Long> inquireKeysByCondition(final List<Condition> conditionList, final List<Order> orderList);

    /**
     * 多条件动态分页查询主键集合，带排序条件,如果有缓存则使用缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<Long> inquirePageKeysByCondition(final List<Condition> conditionList, final List<Order> orderList, final int startIndex, final int rows);

    /**
     * 多条件动态查询主键集合，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<Long> inquireKeysByConditionNoCache(final List<Condition> conditionList, final List<Order> orderList);

    /**
     * 多条件动态分页查询主键集合，带排序条件,无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<Long> inquirePageKeysByConditionNoCache(final List<Condition> conditionList, final List<Order> orderList, final int startIndex, final int rows);

    /**
     * 单个条件动态查询记录总数,如果有缓存则使用缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public int countByColumn(final String columnName, final String columnValue);

    /**
     * 单个条件动态查询记录总数,无缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    public int countByColumnNoCache(final String columnName, final String columnValue);

    /**
     * 两个条件动态查询记录总数,如果有缓存则使用缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public int countByColumns(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 两个条件动态查询记录总数,无缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    public int countByColumnsNoCache(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo);

    /**
     * 多条件动态查询记录总数,如果有缓存则使用缓存
     * @param conditionList
     * @return 
     */
    public int countByCondition(final List<Condition> conditionList);

    /**
     * 多条件动态查询记录总数,无缓存
     * @param conditionList 条件集合
     * @return 
     */
    public int countByConditionNoCache(final List<Condition> conditionList);
}
