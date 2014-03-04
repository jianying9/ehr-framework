package com.ehr.framework.cache;

import com.ehr.framework.util.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * sql key集合查询结果缓存管理对象
 * @author neslon, zoe
 */
public final class SqlCacheImpl implements SqlCache{

    //缓存数据对象
    private final Cache sqlCache;

    public SqlCacheImpl(Cache sqlCache) {
        this.sqlCache = sqlCache;
    }

    /**
     * 将对象放入缓存
     * @param companyId 公司ID
     * @param tableName 表名
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @param keyList key结果集合
     */
    @Override
    public void putKeysCache(final long companyId, final String tableName, final String inquireKeysSql, final String[] values, final List<Long> keyList) {
        StringBuilder sqlBuilder = new StringBuilder(values.length * 10 + inquireKeysSql.length());
        sqlBuilder.append(inquireKeysSql).append('_');
        for (String value : values) {
            sqlBuilder.append(value).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        String md5 = SecurityUtils.encryptByMd5(sqlBuilder.toString());
        StringBuilder keyBuilder = new StringBuilder(tableName.length() + md5.length() + 22);
        keyBuilder.append(Long.toString(companyId)).append('_').append(tableName).append('_').append(md5);
        long[] keyValues = new long[keyList.size()];
        for (int index = 0; index < keyList.size(); index++) {
            keyValues[index] = keyList.get(index);
        }
        Element element = new Element(keyBuilder.toString(), keyValues);
        this.sqlCache.put(element);
    }

    /**
     * 查找缓存
     * @param companyId 公司ID
     * @param tableName 表
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @return 
     */
    @Override
    public List<Long> getKeysCache(final long companyId, final String tableName, final String inquireKeysSql, final String[] values) {
        List<Long> keyList = null;
        StringBuilder sqlBuilder = new StringBuilder(values.length * 10 + inquireKeysSql.length());
        sqlBuilder.append(inquireKeysSql).append('_');
        for (String value : values) {
            sqlBuilder.append(value).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        String md5 = SecurityUtils.encryptByMd5(sqlBuilder.toString());
        StringBuilder keyBuilder = new StringBuilder(tableName.length() + md5.length() + 22);
        keyBuilder.append(Long.toString(companyId)).append('_').append(tableName).append('_').append(md5);
        Element element = this.sqlCache.getQuiet(keyBuilder.toString());
        if (element != null) {
            long[] keyValues = (long[]) element.getObjectValue();
            keyList = new ArrayList<Long>(keyValues.length);
            for (long keyValue : keyValues) {
                keyList.add(keyValue);
            }
        }
        return keyList;
    }

    /**
     * 将对象放入缓存
     * @param companyId 公司ID
     * @param tableName 表名
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @param keyList key结果集合
     */
    @Override
    public void putCountCache(final long companyId, final String tableName, final String countSql, final String[] values, final int count) {
        StringBuilder sqlBuilder = new StringBuilder(values.length * 10 + countSql.length());
        sqlBuilder.append(countSql).append('_');
        for (String value : values) {
            sqlBuilder.append(value).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        String md5 = SecurityUtils.encryptByMd5(sqlBuilder.toString());
        StringBuilder keyBuilder = new StringBuilder(tableName.length() + md5.length() + 22);
        keyBuilder.append(Long.toString(companyId)).append('_').append(tableName).append('_').append(md5);
        Element element = new Element(keyBuilder.toString(), count);
        this.sqlCache.put(element);
    }

    /**
     * 查找缓存
     * @param companyId 公司ID
     * @param tableName 表
     * @param inquireKeysSql key集合查询SQL
     * @param values SQL设置值
     * @return 
     */
    @Override
    public Integer getCountCache(final long companyId, final String tableName, final String countSql, final String[] values) {
        Integer count = null;
        StringBuilder sqlBuilder = new StringBuilder(values.length * 10 + countSql.length());
        sqlBuilder.append(countSql).append('_');
        for (String value : values) {
            sqlBuilder.append(value).append(',');
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        String md5 = SecurityUtils.encryptByMd5(sqlBuilder.toString());
        StringBuilder keyBuilder = new StringBuilder(tableName.length() + md5.length() + 22);
        keyBuilder.append(Long.toString(companyId)).append('_').append(tableName).append('_').append(md5);
        Element element = this.sqlCache.getQuiet(keyBuilder.toString());
        if (element != null) {
            count = (Integer) element.getObjectValue();
        }
        return count;
    }

    /**
     * 移除缓存
     * @param companyId 公司ID
     * @param tableName 表名
     */
    @Override
    public void removeCache(final long companyId, final String tableName) {
        StringBuilder keyHeadBuilder = new StringBuilder(tableName.length() + 22);
        keyHeadBuilder.append(Long.toString(companyId)).append('_').append(tableName);
        String keyHeader = keyHeadBuilder.toString();
        List<String> keyValueList = this.sqlCache.getKeys();
        for (String keyValue : keyValueList) {
            if (keyValue.indexOf(keyHeader) == 0) {
                this.sqlCache.remove(keyValue);
            }
        }
    }
}
