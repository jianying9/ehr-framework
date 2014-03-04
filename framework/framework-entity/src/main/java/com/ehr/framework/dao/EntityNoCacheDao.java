package com.ehr.framework.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * entity dao
 * @author zoe
 */
public final class EntityNoCacheDao<T extends Entity> extends AbstractEntityDao<T> implements EntityDao<T> {

    EntityNoCacheDao() {
    }

    /**
     * 根据主键查询,有缓存
     * @param key
     * @return 
     */
    @Override
    public T inquireByKey(final long keyValue) {
        return this.inquireByKeyNoCache(keyValue);
    }

    /**
     * 根据主键集合查询，有缓存
     * @param keyValues
     * @return 
     */
    @Override
    public List<T> inquireByKeys(final Collection<Long> keyValues) {
        List<T> resultEntityList;
        T t;
        if (keyValues.isEmpty()) {
            resultEntityList = new ArrayList<T>(0);
        } else if (keyValues.size() == 1) {
            resultEntityList = new ArrayList<T>(1);
            t = this.inquireByKeyNoCache(keyValues.iterator().next());
            if (t != null) {
                resultEntityList.add(t);
            }
        } else {
            resultEntityList = this.inquireByKeysNoCache(keyValues);
        }
        return resultEntityList;
    }

    /**
     * 单个条件动态查询，有缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    @Override
    public List<T> inquireByColumn(final String columnName, final String columnValue) {
        return this.inquireByColumnNoCache(columnName, columnValue);
    }

    /**
     * 单个条件动态分页查询，无缓存
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<T> inquirePageByColumn(String columnName, String columnValue, int startIndex, int rows) {
        return this.inquirePageByColumnNoCache(columnName, columnValue, startIndex, rows);
    }

    /**
     * 两个条件动态查询,有缓存
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return 
     */
    @Override
    public List<T> inquireByColumns(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo) {
        return this.inquireByColumnsNoCache(columnName, columnValue, columnNameTwo, columnValueTwo);
    }

    /**
     * 多条件动态查询,有缓存
     * @param conditionList
     * @return 
     */
    @Override
    public List<T> inquireByCondition(final List<Condition> conditionList) {
        return this.inquireByConditionNoCache(conditionList);
    }

    /**
     * 多条件动态分页查询,有缓存
     * @param conditionListfinal
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<T> inquirePageByCondition(List<Condition> conditionListfinal, int startIndex, int rows) {
        return this.inquirePageByConditionNoCache(conditionListfinal, startIndex, rows);
    }

    /**
     * 多条件动态查询，带排序条件,有缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    @Override
    public List<T> inquireByCondition(final List<Condition> conditionList, final List<Order> orderList) {
        return this.inquireByConditionNoCache(conditionList, orderList);
    }

    /**
     * 多条件动态分页查询，带排序条件,有缓存
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<T> inquirePageByCondition(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        return this.inquirePageByConditionNoCache(conditionList, orderList, startIndex, rows);
    }

    /**
     * 插入
     * @param entityMap 
     */
    @Override
    public void insert(final Map<String, String> entityMap) {
        this.insertNoCache(entityMap);
    }

    /**
     * 批量插入
     * @param entityMapList 
     */
    @Override
    public void batchInsert(List<Map<String, String>> entityMapList) {
        if (!entityMapList.isEmpty()) {
            this.batchInsertNoCache(entityMapList);
        }
    }

    /**
     * 插入并查询
     * @param entityMap
     * @return 
     */
    @Override
    public T insertAndInquire(final Map<String, String> entityMap) {
        return this.insertAndInquireNoCache(entityMap);
    }

    /**
     * 更新,并移除相应缓存
     * @param entityMap 
     */
    @Override
    public void update(final Map<String, String> entityMap) {
        this.updateNoCache(entityMap);
    }

    /**
     * 批量更新
     * @param entityMapList 
     */
    @Override
    public void batchUpdate(List<Map<String, String>> entityMapList) {
        if (!entityMapList.isEmpty()) {
            this.batchUpdateNoCache(entityMapList);
        }
    }

    /**
     * 更新并查询后新后值
     * @param entityMap
     * @return insertFields
     */
    @Override
    public T updateAndInquire(Map<String, String> entityMap) {
        return this.updateAndInquireNoCache(entityMap);
    }

    /**
     * 删除,移除缓存
     * @param keyValue 
     */
    @Override
    public void delete(long keyValue) {
        this.deleteNoCache(keyValue);
    }

    /**
     * 批量删除
     * @param keyValues 
     */
    @Override
    public void batchDelete(Collection<Long> keyValues) {
        if (!keyValues.isEmpty()) {
            this.batchDeleteNoCache(keyValues);
        }
    }

    /**
     * 单个条件动态查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    @Override
    public List<Long> inquireKeysByColumn(final String columnName, final String columnValue) {
        return this.inquireKeysByColumnNoCache(columnName, columnValue);
    }

    /**
     * 单个条件动态分页查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<Long> inquirePageKeysByColumn(String columnName, String columnValue, int startIndex, int rows) {
        return this.inquirePageKeysByColumnNoCache(columnName, columnValue, startIndex, rows);
    }

    /**
     * 两个条件动态查询主键集合，无缓存
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return 
     */
    @Override
    public List<Long> inquireKeysByColumns(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        return this.inquireKeysByColumnsNoCache(columnName, columnValue, columnNameTwo, columnValueTwo);
    }

    /**
     * 多个条件动态查询主键集合，无缓存
     * @param conditionList
     * @return 
     */
    @Override
    public List<Long> inquireKeysByCondition(List<Condition> conditionList) {
        return this.inquireKeysByConditionNoCache(conditionList);
    }

    /**
     * 多个条件动态分页查询主键集合，无缓存
     * @param conditionList
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<Long> inquirePageKeysByCondition(List<Condition> conditionList, int startIndex, int rows) {
        return this.inquirePageKeysByConditionNoCache(conditionList, startIndex, rows);
    }

    /**
     * 多个条件动态查询主键集合，可排序，无缓存
     * @param conditionList
     * @param orderList
     * @return 
     */
    @Override
    public List<Long> inquireKeysByCondition(List<Condition> conditionList, List<Order> orderList) {
        return this.inquireKeysByConditionNoCache(conditionList, orderList);
    }

    /**
     * 多个条件动态分页查询主键集合，可排序，无缓存
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return 
     */
    @Override
    public List<Long> inquirePageKeysByCondition(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        return this.inquirePageKeysByConditionNoCache(conditionList, orderList, startIndex, rows);
    }

    /**
     * 单个条件动态查询总记录数，无缓存
     * @param columnName
     * @param columnValue
     * @return 
     */
    @Override
    public int countByColumn(String columnName, String columnValue) {
        return this.countByColumnNoCache(columnName, columnValue);
    }

    /**
     * 两个条件动态查询总记录数，无缓存
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return 
     */
    @Override
    public int countByColumns(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        return this.countByColumnsNoCache(columnName, columnValue, columnNameTwo, columnValueTwo);
    }

    /**
     * 多个条件动态查询总记录数，无缓存
     * @param conditionList
     * @return 
     */
    @Override
    public int countByCondition(List<Condition> conditionList) {
        return this.countByConditionNoCache(conditionList);
    }
}
