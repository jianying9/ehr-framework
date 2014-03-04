package com.ehr.framework.view;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.dao.View;
import com.ehr.framework.dao.ViewDao;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局信息构造类
 * @author zoe
 */
public class ViewContextBuilderImpl<T extends Entity, V extends View> implements ViewContextBuilder<T, V> {

    //viewDAO集合
    private final Map<String, ViewDao<V>> viewDaoMap;
    private final DataSourceContextBuilder dataSourceContextBuilder;
    private final EntityContextBuilder<T> entityContextBuilder;
    private final Map<String, String> existClassMap = new HashMap<String, String>(128);

    @Override
    public final void putViewDao(final String viewName, final ViewDao<V> viewDao, final String className) {
        if (this.viewDaoMap.containsKey(viewName)) {
            String existClassName = this.existClassMap.get(viewName);
            if (existClassName == null) {
                existClassName = "NULL";
            }
            StringBuilder errBuilder = new StringBuilder(1024);
            errBuilder.append("There was an error putting view dao. Cause: viewName reduplicated : ")
                    .append(viewName).append("\n").append("exist class : ").append(existClassName)
                    .append("\n").append("this class : ").append(className);
            throw new RuntimeException(errBuilder.toString());
        }
        this.viewDaoMap.put(viewName, viewDao);
        this.existClassMap.put(viewName, className);
    }

    @Override
    public final Map<String, ViewDao<V>> getViewDaoMap() {
        return Collections.unmodifiableMap(this.viewDaoMap);
    }

    /**
     * 构造函数
     * @param properties 
     */
    public ViewContextBuilderImpl(final EntityContextBuilder<T> entityContextBuilder, final DataSourceContextBuilder dataSourceContextBuilder) {
        this.viewDaoMap = new HashMap<String, ViewDao<V>>(16, 1);
        this.entityContextBuilder = entityContextBuilder;
        this.dataSourceContextBuilder = dataSourceContextBuilder;
    }

    @Override
    public boolean assertExistView(String viewName) {
        return this.viewDaoMap.containsKey(viewName);
    }

    @Override
    public EntityContextBuilder<T> getEntityContextBuilder() {
        return this.entityContextBuilder;
    }

    @Override
    public DataSourceContextBuilder getDataSourceContextBuilder() {
        return this.dataSourceContextBuilder;
    }
}
