package com.ehr.framework.view;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.dao.View;
import com.ehr.framework.dao.ViewDao;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import java.util.Map;

/**
 *
 * @author zoe
 */
public interface ViewContextBuilder<T extends Entity, V extends View> {

    public void putViewDao(final String viewName, final ViewDao<V> viewDao, String className);

    public Map<String, ViewDao<V>> getViewDaoMap();

    public boolean assertExistView(final String viewName);

    public EntityContextBuilder<T> getEntityContextBuilder();

    public DataSourceContextBuilder getDataSourceContextBuilder();
}
