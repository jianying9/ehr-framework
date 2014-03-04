package com.ehr.framework.datasource;

import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author zoe
 */
public interface DataSourceContextBuilder {

    public void putDataSource(String dataSourceName, DataSource dataSource);

    public Map<String, DataSource> getDataSourceMap();

    public DataSource getDataSource(String dataSourceName);
}
