package com.ehr.framework.dao;

import com.ehr.framework.cache.SqlCache;
import com.ehr.framework.context.SessionManager;
import com.ehr.framework.jdbc.SqlBuilder;
import com.ehr.framework.jdbc.SqlExecuter;
import com.ehr.framework.worker.workhandler.UserInfoEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * entity dao
 *
 * @author zoe
 */
public final class EntityCacheDao<T extends Entity> extends AbstractEntityDao<T> implements EntityDao<T> {

    //sql缓存
    private SqlCache sqlCache;
    //entity缓存
    private Cache entityCache;
    private SessionManager sessionManager;

    EntityCacheDao() {
    }

    void setEntityCache(Cache entityCache) {
        this.entityCache = entityCache;
    }

    void setSqlCache(SqlCache sqlCache) {
        this.sqlCache = sqlCache;
    }

    void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 根据keyValue从缓存中获取entity
     *
     * @param keyValue
     * @return
     */
    private T getCacheEntity(final long keyValue) {
        T t = null;
        Element element = this.entityCache.getQuiet(keyValue);
        if (element != null) {
            t = (T) element.getObjectValue();
        }
        return t;
    }

    /**
     * 将entity放入缓存
     *
     * @param t
     */
    private void putEntityCache(final T t) {
        Element element = new Element(t.getKeyValue(), t);
        this.entityCache.put(element, true);
    }

    /**
     * 将keyValue对应的缓存清除
     *
     * @param keyValue
     */
    private void removeEntityCache(final long keyValue) {
        this.entityCache.remove(keyValue, true);
    }

    /**
     * 根据主键查询,有缓存
     *
     * @param key
     * @return
     */
    @Override
    public T inquireByKey(final long keyValue) {
        T t = this.getCacheEntity(keyValue);
        if (t == null) {
            t = this.inquireByKeyNoCache(keyValue);
            if (t != null) {
                this.putEntityCache(t);
            }
        }
        return t;
    }

    /**
     * 根据主键集合查询，有缓存
     *
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
            t = this.inquireByKey(keyValues.iterator().next());
            if (t != null) {
                resultEntityList.add(t);
            }
        } else {
            List<T> cacheEntityList = new ArrayList<T>(keyValues.size());
            resultEntityList = cacheEntityList;
            List<Long> missKeyValueList = new ArrayList<Long>(keyValues.size());
            for (Long keyValue : keyValues) {
                t = this.getCacheEntity(keyValue);
                if (t == null) {
                    missKeyValueList.add(keyValue);
                } else {
                    cacheEntityList.add(t);
                }
            }
            if (!missKeyValueList.isEmpty()) {
                List<T> missEntityList = this.inquireByKeysNoCache(missKeyValueList);
                if (!missEntityList.isEmpty()) {
                    resultEntityList = new ArrayList<T>(keyValues.size());
                    boolean notAdd;
                    for (Long keyValue : keyValues) {
                        notAdd = true;
                        for (T t1 : cacheEntityList) {
                            if (t1.getKeyValue() == keyValue) {
                                resultEntityList.add(t1);
                                notAdd = false;
                                break;
                            }
                        }
                        if (notAdd) {
                            for (T t1 : missEntityList) {
                                if (t1.getKeyValue() == keyValue) {
                                    resultEntityList.add(t1);
                                    this.putEntityCache(t1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultEntityList;
    }

    /**
     * 获取登录信息中的companyId
     *
     * @return
     */
    private long inquireCompanyIdOnSession() {
        long companyId = -1;
        if (this.sessionManager != null) {
            UserInfoEntity userInfoEntity = this.sessionManager.getThreadLocal();
            if (userInfoEntity != null) {
                companyId = userInfoEntity.getCompanyId();
            }
        }
        return companyId;
    }

    /**
     * keys 查询缓存
     *
     * @param inquireKeysSql
     * @param values
     * @return
     */
    private List<Long> inquireKeysCache(final String inquireKeysSql, final String[] values) {
        long companyId = this.inquireCompanyIdOnSession();
        List<Long> keyValueList = this.sqlCache.getKeysCache(companyId, this.tableName, inquireKeysSql, values);
        if (keyValueList == null) {
            keyValueList = SqlExecuter.inquireKeys(this.dataSource, inquireKeysSql, values);
            this.sqlCache.putKeysCache(companyId, this.tableName, inquireKeysSql, values, keyValueList);
        }
        return keyValueList;
    }

    /**
     * count查询缓存
     *
     * @param inquireKeysSql
     * @param values
     * @return
     */
    private int countCache(final String countSql, final String[] values) {
        long companyId = this.inquireCompanyIdOnSession();
        Integer count = this.sqlCache.getCountCache(companyId, this.tableName, countSql, values);
        if (count == null) {
            count = SqlExecuter.count(this.dataSource, countSql, values);
            this.sqlCache.putCountCache(companyId, this.tableName, countSql, values, count);
        }
        return count;
    }

    /**
     * 移除keys缓存
     */
    private void removeSqlCache() {
        long companyId = this.inquireCompanyIdOnSession();
        this.sqlCache.removeCache(companyId, this.tableName);
    }

    /**
     * 单个条件动态查询，有缓存
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    @Override
    public List<T> inquireByColumn(final String columnName, final String columnValue) {
        List<Long> keyValueList = this.inquireKeysByColumn(columnName, columnValue);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 单个条件动态查询，有缓存
     *
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return
     */
    @Override
    public List<T> inquirePageByColumn(String columnName, String columnValue, int startIndex, int rows) {
        List<Long> keyValueList = this.inquirePageKeysByColumn(columnName, columnValue, startIndex, rows);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 两个条件动态查询,有缓存
     *
     * @param columnName 列1
     * @param columnValue 值1
     * @param columnNameTwo 列2
     * @param columnValueTwo 值2
     * @return
     */
    @Override
    public List<T> inquireByColumns(final String columnName, final String columnValue, final String columnNameTwo, final String columnValueTwo) {
        List<Long> keyValueList = this.inquireKeysByColumns(columnName, columnValue, columnNameTwo, columnValueTwo);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 多条件动态查询,有缓存
     *
     * @param conditionList
     * @return
     */
    @Override
    public List<T> inquireByCondition(final List<Condition> conditionList) {
        List<Long> keyValueList = this.inquireKeysByCondition(conditionList);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 多条件动态分页查询,有缓存
     *
     * @param conditionListfinal
     * @param startIndex
     * @param rows
     * @return
     */
    @Override
    public List<T> inquirePageByCondition(List<Condition> conditionListfinal, int startIndex, int rows) {
        List<Long> keyValueList = this.inquirePageKeysByCondition(conditionListfinal, startIndex, rows);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 多条件动态查询，带排序条件,有缓存
     *
     * @param conditionList
     * @param orderList
     * @return
     */
    @Override
    public List<T> inquireByCondition(final List<Condition> conditionList, final List<Order> orderList) {
        List<Long> keyValueList = this.inquireKeysByCondition(conditionList, orderList);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 多条件动态分页查询，带排序条件,有缓存
     *
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rowsremoveKeysCache
     * @return
     */
    @Override
    public List<T> inquirePageByCondition(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        List<Long> keyValueList = this.inquirePageKeysByCondition(conditionList, orderList, startIndex, rows);
        return this.inquireByKeys(keyValueList);
    }

    /**
     * 插入
     *
     * @param entityMap
     */
    @Override
    public void insert(final Map<String, String> entityMap) {
        this.insertNoCache(entityMap);
        this.removeSqlCache();
    }

    @Override
    public void batchInsert(List<Map<String, String>> entityMapList) {
        if (!entityMapList.isEmpty()) {
            this.batchInsertNoCache(entityMapList);
            this.removeSqlCache();
        }
    }

    /**
     * 插入并查询
     *
     * @param entityMap
     * @return
     */
    @Override
    public T insertAndInquire(final Map<String, String> entityMap) {
        final T t = this.insertAndInquireNoCache(entityMap);
        this.removeSqlCache();
        this.putEntityCache(t);
        return t;
    }

    /**
     * 更新,并移除相应缓存
     *
     * @param entityMap
     */
    @Override
    public void update(final Map<String, String> entityMap) {
        if (this.assertCanUpdate(entityMap)) {
            long keyValue = Long.parseLong(entityMap.get(this.keyField));
            String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
            String[] values = this.getUpdateValues(entityMap);
            SqlExecuter.update(this.dataSource, updateSql, values);
            this.removeEntityCache(keyValue);
            this.removeSqlCache();
        }
    }

    /**
     *
     * @param entityMapList
     */
    @Override
    public void batchUpdate(List<Map<String, String>> entityMapList) {
        if (!entityMapList.isEmpty()) {
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
                List<Long> keyValueList = new ArrayList<Long>(validEntityMapList.size());
                String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
                String[][] valueArrays = new String[entityMapList.size()][];
                for (int index = 0; index < validEntityMapList.size(); index++) {
                    entityMap = entityMapList.get(index);
                    valueArrays[index] = this.getUpdateValues(entityMap);
                    keyValueList.add(Long.parseLong(entityMap.get(this.keyField)));
                }
                SqlExecuter.batchUpdate(this.dataSource, updateSql, valueArrays);
                this.removeSqlCache();
                for (long keyValue : keyValueList) {
                    this.removeEntityCache(keyValue);
                }
            }
        }
    }

    /**
     * 更新并查询后新后值
     *
     * @param entityMap
     * @return
     */
    @Override
    public T updateAndInquire(Map<String, String> entityMap) {
        T t = null;
        if (this.assertCanUpdate(entityMap)) {
            String updateSql = SqlBuilder.updataSqlBuild(this.tableName, this.updateFields, entityMap, this.keyField);
            String[] values = this.getUpdateValues(entityMap);
            Map<String, String> resultMap = SqlExecuter.updateAndInquireByKey(this.dataSource, updateSql, values, this.inquireByKeySql);
            if (resultMap != null) {
                t = this.newInstance(resultMap);
                this.putEntityCache(t);
            }
            this.removeSqlCache();
        }
        return t;
    }

    /**
     * 删除,移除缓存
     *
     * @param keyValue
     */
    @Override
    public void delete(long keyValue) {
        this.deleteNoCache(keyValue);
        this.removeEntityCache(keyValue);
        this.removeSqlCache();
    }

    /**
     * 批量删除,移除缓存
     *
     * @param keyValues
     */
    @Override
    public void batchDelete(Collection<Long> keyValues) {
        if (!keyValues.isEmpty()) {
            this.batchDeleteNoCache(keyValues);
            this.removeSqlCache();
            for (Long keyValue : keyValues) {
                this.removeEntityCache(keyValue);
            }
        }
    }

    /**
     * 单个条件动态查询主键集合，使用缓存
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    @Override
    public List<Long> inquireKeysByColumn(final String columnName, final String columnValue) {
        final String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, columnName);
        final String[] values = {columnValue};
        return this.inquireKeysCache(inquireKeysSql, values);
    }

    /**
     * 两个条件动态查询主键集合,有缓存
     *
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return
     */
    @Override
    public List<Long> inquireKeysByColumns(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        final String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, columnName, columnNameTwo);
        final String[] values = {columnValue, columnValueTwo};
        return this.inquireKeysCache(inquireKeysSql, values);
    }

    /**
     * 多条件动态查询主键集合,有缓存
     *
     * @param conditionList
     * @return
     */
    @Override
    public List<Long> inquireKeysByCondition(List<Condition> conditionList) {
        List<Long> keyList = null;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            final String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, conditionList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = this.inquireKeysCache(inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 多条件动态查询主键查询，带排序条件,有缓存
     *
     * @param conditionList
     * @param orderList
     * @return
     */
    @Override
    public List<Long> inquireKeysByCondition(List<Condition> conditionList, List<Order> orderList) {
        List<Long> keyList = null;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            final String inquireKeysSql = SqlBuilder.inquireKeysSqlBuild(this.tableName, this.keyField, conditionList, orderList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = this.inquireKeysCache(inquireKeysSql, values);
        }
        return keyList;
    }

    /**
     * 单个条件动态查询总记录数，有缓存
     *
     * @param columnName
     * @param columnValue
     * @return
     */
    @Override
    public int countByColumn(String columnName, String columnValue) {
        String countSql = SqlBuilder.countSqlBuild(this.tableName, columnName);
        String[] values = {columnValue};
        return this.countCache(countSql, values);
    }

    /**
     * 两个条件动态查询总记录数，有缓存
     *
     * @param columnName
     * @param columnValue
     * @param columnNameTwo
     * @param columnValueTwo
     * @return
     */
    @Override
    public int countByColumns(String columnName, String columnValue, String columnNameTwo, String columnValueTwo) {
        String countSql = SqlBuilder.countSqlBuild(this.tableName, columnName, columnNameTwo);
        String[] values = {columnValue, columnValueTwo};
        return this.countCache(countSql, values);
    }

    /**
     * 多个条件动态查询总记录数，有缓存
     *
     * @param conditionList
     * @return
     */
    @Override
    public int countByCondition(List<Condition> conditionList) {
        int count = 0;
        if (!conditionList.isEmpty()) {
            String countSql = SqlBuilder.countSqlBuild(this.tableName, conditionList);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            count = this.countCache(countSql, values);
        }
        return count;
    }

    /**
     * 单个条件动态分页查询主键集合，有缓存
     *
     * @param columnName
     * @param columnValue
     * @param startIndex
     * @param rows
     * @return
     */
    @Override
    public List<Long> inquirePageKeysByColumn(String columnName, String columnValue, int startIndex, int rows) {
        String inquirePageKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, columnName, startIndex, rows);
        String[] values = {columnValue};
        return this.inquireKeysCache(inquirePageKeysSql, values);
    }

    /**
     * 多个条件动态分页查询主键集合，有缓存
     *
     * @param conditionList
     * @param startIndex
     * @param rows
     * @return
     */
    @Override
    public List<Long> inquirePageKeysByCondition(List<Condition> conditionList, int startIndex, int rows) {
        List<Long> keyList = null;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            String inquirePageKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, conditionList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = this.inquireKeysCache(inquirePageKeysSql, values);
        }
        return keyList;
    }

    /**
     * 多个条件动态分页查询主键集合，可排序，有缓存
     *
     * @param conditionList
     * @param orderList
     * @param startIndex
     * @param rows
     * @return
     */
    @Override
    public List<Long> inquirePageKeysByCondition(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        List<Long> keyList = null;
        if (conditionList.isEmpty()) {
            keyList = new ArrayList<Long>(0);
        } else {
            String inquirePageKeysSql = SqlBuilder.inquirePageKeysSqlBuild(this.tableName, this.keyField, conditionList, orderList, startIndex, rows);
            String[] values = SqlBuilder.getConditionValues(conditionList);
            keyList = this.inquireKeysCache(inquirePageKeysSql, values);
        }
        return keyList;
    }
}
