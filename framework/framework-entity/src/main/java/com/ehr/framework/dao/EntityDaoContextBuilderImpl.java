package com.ehr.framework.dao;

import com.ehr.framework.cache.DefaultCacheConfiguration;
import com.ehr.framework.cache.SqlCache;
import com.ehr.framework.cache.SqlCacheImpl;
import com.ehr.framework.context.SessionManager;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * 全局信息构造类
 *
 * @author zoe
 */
public class EntityDaoContextBuilderImpl<T extends Entity> implements EntityDaoContextBuilder<T> {

    //缓存管理对象
    private final CacheManager cacheManager;
    private final DataSourceContextBuilder dataSourceContextBuilder;
    private final SessionManager sessionManager;
    private final Map<String, String> existClassMap = new HashMap<String, String>(128);

    @Override
    public final CacheManager getCacheManager() {
        return cacheManager;
    }
    //sql查询缓存对象
    private SqlCache sqlCache;

    @Override
    public final SqlCache getSqlCache() {
        return this.sqlCache;
    }
    //entity处理类集合
    private final Map<String, EntityDao<T>> entityDaoMap;

    @Override
    public final void putEntityDao(final String entityName, final EntityDao<T> entityDao, String className) {
        if (this.entityDaoMap.containsKey(entityName)) {
            String existClassName = this.existClassMap.get(entityName);
            if (existClassName == null) {
                existClassName = "NULL";
            }
            StringBuilder errBuilder = new StringBuilder(1024);
            errBuilder.append("There was an error putting entityDao. Cause: entityName reduplicated : ").append(entityName).append("\n").append("exist class : ").append(existClassName).append("\n").append("this class : ").append(className);
            throw new RuntimeException(errBuilder.toString());
        }
        this.entityDaoMap.put(entityName, entityDao);
        this.existClassMap.put(entityName, className);
    }

    @Override
    public Map<String, EntityDao<T>> getEntityDaoMap() {
        return Collections.unmodifiableMap(this.entityDaoMap);
    }

    /**
     * 构造函数
     *
     * @param properties
     */
    public EntityDaoContextBuilderImpl(final DataSourceContextBuilder dataSourceContextBuilder, final SessionManager sessionManager, final CacheManager cacheManager) {
        this.entityDaoMap = new HashMap<String, EntityDao<T>>(64, 1);
        this.dataSourceContextBuilder = dataSourceContextBuilder;
        this.sessionManager = sessionManager;
        this.cacheManager = cacheManager;
        //创建sql cache
        final CacheConfiguration sqlCacheConfig = new DefaultCacheConfiguration().getDefault();
        String uuid = UUID.randomUUID().toString();
        String sqlCacheName = "sql-cache-".concat(uuid);
        sqlCacheConfig.name(sqlCacheName).maxElementsInMemory(20000);
        final Cache cache = new Cache(sqlCacheConfig);
        this.cacheManager.addCache(cache);
        this.sqlCache = new SqlCacheImpl(cache);
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    public boolean assertExistEntity(String entityName) {
        return this.entityDaoMap.containsKey(entityName);
    }

    @Override
    public DataSourceContextBuilder getDataSourceContextBuilder() {
        return this.dataSourceContextBuilder;
    }

    @Override
    public EntityDao getEntityDao(String entityName) {
        return this.entityDaoMap.get(entityName);
    }
}
