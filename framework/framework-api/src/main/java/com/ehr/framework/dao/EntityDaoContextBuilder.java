package com.ehr.framework.dao;

import com.ehr.framework.cache.SqlCache;
import com.ehr.framework.context.SessionManager;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import java.util.Map;
import net.sf.ehcache.CacheManager;

/**
 *
 * @author zoe
 */
public interface EntityDaoContextBuilder<T extends Entity> {

    public SqlCache getSqlCache();

    public CacheManager getCacheManager();

    public void putEntityDao(final String entityName, final EntityDao<T> entityDao, String className);
    
    public EntityDao getEntityDao(final String entityName);

    public Map<String, EntityDao<T>> getEntityDaoMap();

    public SessionManager getSessionManager();

    public boolean assertExistEntity(final String entityName);

    public DataSourceContextBuilder getDataSourceContextBuilder();
}
