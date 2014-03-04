package com.ehr.framework.dao;

import com.ehr.framework.cache.DefaultCacheConfiguration;
import com.ehr.framework.cache.SqlCache;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import com.ehr.framework.entity.EntityConfig;
import com.ehr.framework.logger.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;

/**
 * 负责解析annotation EntityConfig
 * @author zoe
 */
public class EntityConfigDaoParser<T extends Entity> {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param <T>
     * @param clazz 
     */
    public void parse(final Class<T> clazz, final EntityDaoContextBuilder<T> entityDaoCtxBuilder) {
        this.logger.debug("-----------------parsing entity DAO {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(EntityConfig.class)) {
            //1.获取注解EntityConfig
            final EntityConfig entityConfig = clazz.getAnnotation(EntityConfig.class);
            //2.获取dataSource
            final String dataSourceName = entityConfig.dataSourceName();
            final DataSourceContextBuilder dataSourceContextBuilder = entityDaoCtxBuilder.getDataSourceContextBuilder();
            final DataSource dataSource = dataSourceContextBuilder.getDataSource(dataSourceName);
            //3.获取实体标识
            final String entityName = entityConfig.entityName();
            //4.获取key
            final String keyField = entityConfig.keyField();
            //5获取该实体所有字段集合
            Field[] fieldTemp = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList<Field>(fieldTemp.length);
            int modifier;
            for (Field field : fieldTemp) {
                modifier = field.getModifiers();
                if (!Modifier.isStatic(modifier)) {
                    fieldList.add(field);
                }
            }
            Field[] fields = fieldList.toArray(new Field[fieldList.size()]);
            //6.设置查询集合,为entity的所有field
            final String[] selectFields = new String[fields.length];
            for (int index = 0; index < fields.length; index++) {
                selectFields[index] = fields[index].getName();
            }
            //7.设置查询集合
            //7.1获取配置插入集合和插入扩展集合
            String[] configInsertFields = entityConfig.insertFields();
            final String[] configInsertExtendFields = entityConfig.insertExtendFields();
            //7.2如果用户配置插入集合长度为0，则默认为entity的所有field
            if (configInsertFields.length == 0) {
                configInsertFields = selectFields;
            }
            //7.3合并插入集合和扩展插入集合，排除keyField,不能重复
            Set<String> insertFieldSet = new HashSet<String>(configInsertFields.length + configInsertExtendFields.length);
            for (String fieldName : configInsertFields) {
                if (!fieldName.equals(keyField)) {
                    insertFieldSet.add(fieldName);
                }
            }
            for (String fieldName : configInsertExtendFields) {
                if (!fieldName.equals(keyField)) {
                    insertFieldSet.add(fieldName);
                }
            }
            final String[] insertFields = insertFieldSet.toArray(new String[insertFieldSet.size()]);
            //8.设置更新集合
            //8.1获取配置更新集合和扩展更新集合
            String[] configUpdateFields = entityConfig.updateFields();
            final String[] configUpdateExtendFields = entityConfig.updateExtendFields();
            //8.2如果用户配置更新集合长度为0，则默认为entity的所有field
            if (configUpdateFields.length == 0) {
                configUpdateFields = selectFields;
            }
            //8.3合并更新集合和扩展更新集合，排除keyField，不能重复
            Set<String> updateFieldSet = new HashSet<String>(configUpdateFields.length + configUpdateFields.length);
            for (String fieldName : configUpdateFields) {
                if (!fieldName.equals(keyField)) {
                    updateFieldSet.add(fieldName);
                }
            }
            for (String fieldName : configUpdateExtendFields) {
                if (!fieldName.equals(keyField)) {
                    updateFieldSet.add(fieldName);
                }
            }
            final String[] updateFields = updateFieldSet.toArray(new String[updateFieldSet.size()]);
            final boolean useCache = entityConfig.useCache();
            //9.获取sql-cache
            SqlCache sqlCache = null;
            //10.创建entityCache
            Cache entityCache = null;
            if (useCache) {
                if (entityDaoCtxBuilder.getCacheManager().cacheExists(entityName)) {
                    StringBuilder mesBuilder = new StringBuilder(512);
                    mesBuilder.append("There was an error parsing entity cache. Cause: exist cache name : ").append(entityName);
                    mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                    throw new RuntimeException(mesBuilder.toString());
                }
                sqlCache = entityDaoCtxBuilder.getSqlCache();
                final int maxElementsInMemory = entityConfig.maxElementsInMemory();
                final int timeToIdleSeconds = entityConfig.timeToIdleSeconds();
                final int timeToLiveSeconds = entityConfig.timeToLiveSeconds();
                final CacheConfiguration entityCacheConfig = new DefaultCacheConfiguration().getDefault();
                entityCacheConfig.name(entityName).maxElementsInMemory(maxElementsInMemory).timeToIdleSeconds(timeToIdleSeconds).timeToLiveSeconds(timeToLiveSeconds);
                entityCache = new Cache(entityCacheConfig);
                entityDaoCtxBuilder.getCacheManager().addCache(entityCache);
            }
            //11.创建该class的entityDao
            EntityDaoBuilder<T> entityDaoBuilder = new EntityDaoBuilder<T>();
            entityDaoBuilder.setClazz(clazz).setTableName(entityName).setDataSource(dataSource).setInsertFields(insertFields).setKeyField(keyField).setSelectFields(selectFields).setUpdateFields(updateFields).setSqlCache(sqlCache).setEntityCache(entityCache).setUseCache(useCache).setSessionManager(entityDaoCtxBuilder.getSessionManager());
            EntityDao<T> entityDao = entityDaoBuilder.build();
            entityDaoCtxBuilder.putEntityDao(entityName, entityDao, clazz.getName());
            this.logger.debug("-----------------parse entity DAO {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse entity DAO {} missing annotation EntityConfig-----------------", clazz.getName());
        }
    }
}
