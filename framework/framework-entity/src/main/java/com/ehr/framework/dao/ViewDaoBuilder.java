package com.ehr.framework.dao;

import com.ehr.framework.jdbc.Column;
import com.ehr.framework.jdbc.MainTable;
import com.ehr.framework.jdbc.RelationalTable;
import com.ehr.framework.jdbc.SqlBuilder;
import java.util.List;
import javax.sql.DataSource;

/**
 * 视图数据访问对象创建类
 * @author zoe
 */
public final class ViewDaoBuilder<V extends View> {

    //数据源
    private DataSource dataSource;
    private MainTable mainTable;
    private List<RelationalTable> relationalTableList;
    private List<Column> columnList;
    //实体class
    private Class<V> clazz;

    public ViewDaoBuilder<V> setClazz(Class<V> clazz) {
        this.clazz = clazz;
        return this;
    }

    public ViewDaoBuilder<V> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public ViewDaoBuilder<V> setMainTable(MainTable mainTable) {
        this.mainTable = mainTable;
        return this;
    }

    public ViewDaoBuilder<V> setRelationalTables(List<RelationalTable> relationalTableList) {
        this.relationalTableList = relationalTableList;
        return this;
    }

    public ViewDaoBuilder<V> setColumnList(List<Column> columnList) {
        this.columnList = columnList;
        return this;
    }
    
    public ViewDao<V> build() {
        if (this.mainTable == null) {
            throw new RuntimeException("There was an error building viewDao. Cause: mainTable is null");
        }
        if (this.clazz == null) {
            throw new RuntimeException("There was an error building viewDao. Cause: clazz is null");
        }
        if (this.dataSource == null) {
            throw new RuntimeException("There was an error building viewDao. Cause: dataSource is null");
        }
        if (this.relationalTableList == null || this.relationalTableList.isEmpty()) {
            throw new RuntimeException("There was an error building viewDao. Cause: relationalTables is null or empty");
        }
        ViewNoCacheDao<V> viewNoCacheDao = new ViewNoCacheDao<V>();
        viewNoCacheDao.setClazz(this.clazz);
        viewNoCacheDao.setDataSource(this.dataSource);
        final String inquireSqlModel = SqlBuilder.inquireViewSqlModelBuild(this.mainTable, this.relationalTableList, this.columnList);
        final String countSqlModel = SqlBuilder.countViewSqlModelBuild(this.mainTable, this.relationalTableList);
        viewNoCacheDao.setInquireSqlModel(inquireSqlModel);
        viewNoCacheDao.setCountSqlModel(countSqlModel);
        return viewNoCacheDao;
    }
}
