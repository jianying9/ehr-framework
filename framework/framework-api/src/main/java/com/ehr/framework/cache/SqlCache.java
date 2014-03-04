package com.ehr.framework.cache;

import java.util.List;

/**
 * sql key集合查询结果缓存管理对象
 * @author neslon, zoe
 */
public interface SqlCache {

    /**
     * 将对象放入缓存
     * @param companyId 公司ID
     * @param tableName 表名
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @param keyList key结果集合
     */
    public void putKeysCache(final long companyId, final String tableName, final String inquireKeysSql, final String[] values, final List<Long> keyList);

    /**
     * 查找缓存
     * @param companyId 公司ID
     * @param tableName 表
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @return 
     */
    public List<Long> getKeysCache(final long companyId, final String tableName, final String inquireKeysSql, final String[] values);

    /**
     * 将对象放入缓存
     * @param companyId 公司ID
     * @param tableName 表名
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @param keyList key结果集合
     */
    public void putCountCache(final long companyId, final String tableName, final String countSql, final String[] values, final int count);

    /**
     * 查找缓存
     * @param companyId 公司ID
     * @param tableName 表
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @return 
     */
    public Integer getCountCache(final long companyId, final String tableName, final String countSql, final String[] values);

    /**
     * 移除缓存
     * @param companyId 公司ID
     * @param tableName 表名
     */
    public void removeCache(final long companyId, final String tableName);
}
