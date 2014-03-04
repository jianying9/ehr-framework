package com.ehr.framework.dao;

import com.ehr.framework.cache.SqlCache;
import com.ehr.framework.context.SessionManager;
import com.ehr.framework.jdbc.SqlBuilder;
import javax.sql.DataSource;
import net.sf.ehcache.Cache;

/**
 * 实体数据访问对象创建类
 * @author zoe
 */
public final class EntityDaoBuilder<T extends Entity> {

    //数据源
    private DataSource dataSource;
    //table name
    private String tableName;
    //key
    private String keyField;
    //查询集合
    private String[] selectFields;
    //插入集合
    private String[] insertFields;
    //更新集合
    private String[] updateFields;
    //实体class
    private Class<T> clazz;
    //sql缓存
    private SqlCache sqlCache;
    //entity缓存
    private Cache entityCache;
    //是否使用缓存
    private boolean useCache;
    //登录信息管理对象
    private SessionManager sessionManager;

    public EntityDaoBuilder<T> setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        return this;
    }

    public EntityDaoBuilder<T> setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public EntityDaoBuilder<T> setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public EntityDaoBuilder<T> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public EntityDaoBuilder<T> setInsertFields(String[] insertFields) {
        this.insertFields = insertFields;
        return this;
    }

    public EntityDaoBuilder<T> setKeyField(String keyField) {
        this.keyField = keyField;
        return this;
    }

    public EntityDaoBuilder<T> setSelectFields(String[] selectFields) {
        this.selectFields = selectFields;
        return this;
    }

    public EntityDaoBuilder<T> setUpdateFields(String[] updateFields) {
        this.updateFields = updateFields;
        return this;
    }

    public EntityDaoBuilder<T> setEntityCache(Cache entityCache) {
        this.entityCache = entityCache;
        return this;
    }

    public EntityDaoBuilder<T> setSqlCache(SqlCache sqlCache) {
        this.sqlCache = sqlCache;
        return this;
    }

    public EntityDaoBuilder<T> setUseCache(boolean useCache) {
        this.useCache = useCache;
        return this;
    }

    public EntityDao<T> build() {
        if (this.tableName == null || this.tableName.isEmpty()) {
            throw new RuntimeException("There was an error building entityDao. Cause: tableName is null or empty");
        }
        if (this.clazz == null) {
            throw new RuntimeException("There was an error building entityDao. Cause: clazz is null");
        }
        if (this.dataSource == null) {
            throw new RuntimeException("There was an error building entityDao. Cause: dataSource is null");
        }
        if (this.keyField == null || this.keyField.isEmpty()) {
            throw new RuntimeException("There was an error building entityDao. Cause: keyField is null or empty");
        }
        if (this.insertFields == null || this.insertFields.length == 0) {
            throw new RuntimeException("There was an error building entityDao. Cause: insertFields is null or empty");
        }
        if (this.selectFields == null || this.selectFields.length == 0) {
            throw new RuntimeException("There was an error building entityDao. Cause: selectFields is null or empty");
        }
        if (this.updateFields == null || this.updateFields.length == 0) {
            throw new RuntimeException("There was an error building entityDao. Cause: updateFields is null or empty");
        }
        EntityDao<T> entityDao;
        final String inquireSqlModel = SqlBuilder.inquireSqlModelBuild(this.tableName, this.selectFields);
        String insertSql = SqlBuilder.insertSqlBuild(this.tableName, this.insertFields);
        String inquireByKeySql = SqlBuilder.inquireSqlBuild(inquireSqlModel, this.keyField);
        String deleteSql = SqlBuilder.deleteSqlBuild(this.tableName, this.keyField);
        if (this.useCache) {
            if (this.entityCache == null) {
                throw new RuntimeException("There was an error building entityDao. Cause: entityCache is null");
            }
            if (this.sqlCache == null) {
                throw new RuntimeException("There was an error building entityDao. Cause: sqlCache is null");
            }
            EntityCacheDao<T> entityCacheDao = new EntityCacheDao<T>();
            entityCacheDao.setSessionManager(this.sessionManager);
            entityCacheDao.setClazz(this.clazz);
            entityCacheDao.setTableName(this.tableName);
            entityCacheDao.setDataSource(this.dataSource);
            entityCacheDao.setKeyField(this.keyField);
            entityCacheDao.setInsertFields(this.insertFields);
            entityCacheDao.setUpdateFields(this.updateFields);
            entityCacheDao.setSqlCache(this.sqlCache);
            entityCacheDao.setEntityCache(this.entityCache);
            entityCacheDao.setInquireSqlModel(inquireSqlModel);
            entityCacheDao.setInsertSql(insertSql);
            entityCacheDao.setInquireByKeySql(inquireByKeySql);
            entityCacheDao.setDeleteSql(deleteSql);
            entityDao = entityCacheDao;
        } else {
            EntityNoCacheDao<T> entityNoCacheDao = new EntityNoCacheDao<T>();
            entityNoCacheDao.setClazz(this.clazz);
            entityNoCacheDao.setTableName(this.tableName);
            entityNoCacheDao.setDataSource(this.dataSource);
            entityNoCacheDao.setKeyField(this.keyField);
            entityNoCacheDao.setInsertFields(this.insertFields);
            entityNoCacheDao.setUpdateFields(this.updateFields);
            entityNoCacheDao.setInquireSqlModel(inquireSqlModel);
            entityNoCacheDao.setInsertSql(insertSql);
            entityNoCacheDao.setInquireByKeySql(inquireByKeySql);
            entityNoCacheDao.setDeleteSql(deleteSql);
            entityDao = entityNoCacheDao;
        }
        return entityDao;
    }
}
