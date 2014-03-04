package com.ehr.framework.datasource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author zoe
 */
public class DataSourceContextBuilderImpl implements DataSourceContextBuilder {

    private final Map<String, DataSource> dataSourceMap;

    public DataSourceContextBuilderImpl() {
        this.dataSourceMap = new HashMap<String, DataSource>(8, 1);
    }

    @Override
    public void putDataSource(String dataSourceName, DataSource dataSource) {
        if (this.dataSourceMap.containsKey(dataSourceName)) {
            throw new RuntimeException("There was an error putting dataSource. Cause: dataSourceName reduplicated, ".concat(dataSourceName));
        }
        this.dataSourceMap.put(dataSourceName, dataSource);
    }

    @Override
    public Map<String, DataSource> getDataSourceMap() {
        return Collections.unmodifiableMap(this.dataSourceMap);
    }

    @Override
    public DataSource getDataSource(String dataSourceName) {
        return this.dataSourceMap.get(dataSourceName);
    }
}
