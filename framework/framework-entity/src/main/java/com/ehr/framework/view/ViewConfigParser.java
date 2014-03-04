package com.ehr.framework.view;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.dao.View;
import com.ehr.framework.dao.ViewDao;
import com.ehr.framework.dao.ViewDaoBuilder;
import com.ehr.framework.datasource.DataSourceContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import com.ehr.framework.entity.EntityHandler;
import com.ehr.framework.jdbc.Column;
import com.ehr.framework.jdbc.MainTable;
import com.ehr.framework.jdbc.RelationalTable;
import com.ehr.framework.logger.LogFactory;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.*;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 * 负责解析annotation ViewConfig
 * @author zoe
 */
public class ViewConfigParser<T extends Entity, V extends View> {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param <V>
     * @param clazz 
     */
    public void parse(final Class<V> clazz, final ViewContextBuilder<T, V> viewCtxBuilder) {
        this.logger.debug("-----------------parsing view {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(ViewConfig.class)) {
            //1.获取注解ViewConfig
            final ViewConfig viewConfig = clazz.getAnnotation(ViewConfig.class);
            //2.获取dataSource
            final String dataSourceName = viewConfig.dataSourceName();
            final DataSourceContextBuilder dataSourceContextBuilder = viewCtxBuilder.getDataSourceContextBuilder();
            final DataSource dataSource = dataSourceContextBuilder.getDataSource(dataSourceName);
            //3.获取实体标识
            String viewName = viewConfig.viewName();
            this.logger.debug("view identifier : {}", viewName);
            //4.解析主表信息
            MainEntityConfig mainEntityConfig = viewConfig.mainEntity();
            //5.解析关联表
            RelationalEntityConfig[] relationalEntityConfigs = viewConfig.relationalEntitys();
            //6.获取该实体所有字段集合
            final Field[] fields = clazz.getDeclaredFields();
            final Set<String> fieldNameSet = new HashSet<String>(fields.length, 1);
            for (Field field : fields) {
                fieldNameSet.add(field.getName());
            }
            //9.获取view信息并且验证返回字段名称是否有重复,表名是否有重复,表别名是否有重复
            final Map<String, Column> viewFieldMap = new HashMap<String, Column>(16, 1);
            final Set<String> tableNameSet = new HashSet<String>(8, 1);
            final Set<String> tableAliasNameSet = new HashSet<String>(8, 1);
            String tableAliasName;
            String entityName;
            ViewFieldConfig[] viewFieldConfigs;
            //9.1获取主表信息
            entityName = mainEntityConfig.entityName();
            if (tableNameSet.contains(entityName)) {
                this.logger.error("There was an error parsing ViewConfig. Cause: tableName {} is repeatable.", entityName);
                throw new RuntimeException("There was an error parsing ViewConfig. Cause: tableName is repeatable..");
            } else {
                tableNameSet.add(entityName);
            }
            tableAliasName = mainEntityConfig.aliasEntityName();
            if (tableAliasNameSet.contains(tableAliasName)) {
                this.logger.error("There was an error parsing ViewConfig. Cause: tableAliasName {} is repeatable.", tableAliasName);
                throw new RuntimeException("There was an error parsing ViewConfig. Cause: tableAliasName is repeatable..");
            } else {
                tableAliasNameSet.add(tableAliasName);
            }
            viewFieldConfigs = mainEntityConfig.viewFieldConfigs();
            MainTable mainTable = new MainTable();
            mainTable.setTableName(entityName);
            mainTable.setAliasName(tableAliasName);
            this.parseViewField(fieldNameSet, entityName, tableAliasName, viewFieldConfigs, viewFieldMap, viewCtxBuilder);
            //9.2获取关联表信息
            List<RelationalTable> relationalTableList = new ArrayList<RelationalTable>(relationalEntityConfigs.length);
            RelationalTable relationalTable;
            String[] keys;
            String[] mainKeys;
            boolean inner;
            for (RelationalEntityConfig relationalEntityConfig : relationalEntityConfigs) {
                entityName = relationalEntityConfig.entityName();
                if (tableNameSet.contains(entityName)) {
                    this.logger.error("There was an error parsing ViewConfig. Cause: tableName {} is repeatable.", entityName);
                    throw new RuntimeException("There was an error parsing ViewConfig. Cause: tableName is repeatable..");
                } else {
                    tableNameSet.add(entityName);
                }
                tableAliasName = relationalEntityConfig.aliasEntityName();
                if (tableAliasNameSet.contains(tableAliasName)) {
                    this.logger.error("There was an error parsing ViewConfig. Cause: tableAliasName {} is repeatable.", tableAliasName);
                    throw new RuntimeException("There was an error parsing ViewConfig. Cause: tableAliasName is repeatable..");
                } else {
                    tableAliasNameSet.add(tableAliasName);
                }
                viewFieldConfigs = relationalEntityConfig.viewFieldConfigs();
                keys = relationalEntityConfig.keys();
                mainKeys = relationalEntityConfig.mainKeys();
                if (keys.length != mainKeys.length) {
                    this.logger.error("There was an error parsing ViewConfig. Cause: {} mainkeys's length is not equal to keys's length.", entityName);
                    throw new RuntimeException("There was an error parsing ViewConfig. Cause: mainkeys's length is not equal to keys's length.");
                }
                inner = relationalEntityConfig.inner();
                relationalTable = new RelationalTable();
                relationalTable.setTableName(entityName);
                relationalTable.setAliasName(tableAliasName);
                relationalTable.setKeys(keys);
                relationalTable.setMainKeys(mainKeys);
                relationalTable.setInner(inner);
                relationalTableList.add(relationalTable);
                this.parseViewField(fieldNameSet, entityName, tableAliasName, viewFieldConfigs, viewFieldMap, viewCtxBuilder);
            }
            //9.3判断是否所有field都包含在view field里面
            for (String fieldName : fieldNameSet) {
                if (!viewFieldMap.containsKey(fieldName)) {
                    this.logger.error("There was an error parsing ViewConfig. Cause: view field {} is missing.", fieldName);
                    throw new RuntimeException("There was an error parsing ViewConfig. Cause: view field is missing.");
                }
            }
            //10.生成查询字段集合
            List<Column> columnList = new ArrayList<Column>(viewFieldMap.size());
            Set<Entry<String, Column>> viewFieldSet = viewFieldMap.entrySet();
            for (Entry<String, Column> entry : viewFieldSet) {
                columnList.add(entry.getValue());
            }
            //11.创建viewDao对象
            ViewDaoBuilder<V> viewDaoBuilder = new ViewDaoBuilder<V>();
            viewDaoBuilder.setClazz(clazz).setDataSource(dataSource).setMainTable(mainTable).setRelationalTables(relationalTableList).setColumnList(columnList);
            ViewDao<V> viewDao = viewDaoBuilder.build();
            //12.保存viewDao
            if (viewCtxBuilder.assertExistView(viewName)) {
                StringBuilder mesBuilder = new StringBuilder(512);
                mesBuilder.append("There was an error putting view dao. Cause: viewName reduplicated : ").append(viewName);
                mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                throw new RuntimeException(mesBuilder.toString());
            }
            viewCtxBuilder.putViewDao(viewName, viewDao, clazz.getName());
            this.logger.debug("-----------------parse view {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse view {} missing annotation ViewConfig-----------------", clazz.getName());
        }
    }

    private void parseViewField(final Set<String> fieldNameSet, final String entityName, final String tableAliasName, final ViewFieldConfig[] viewFieldConfigs, final Map<String, Column> viewFieldMap, final ViewContextBuilder viewCtxBuilder) {
        Column viewField;
        EntityHandler entityHandler;
        final EntityContextBuilder<T> entityContextBuilder = viewCtxBuilder.getEntityContextBuilder();
        if (viewFieldConfigs.length == 0) {
            //没有启用别名,获取原有实体名称
            entityHandler = entityContextBuilder.getEntityHandler(entityName);
            if (entityHandler == null) {
                this.logger.error("There was an error parsing ViewConfig. Cause: entity {} was not found.", entityName);
                throw new RuntimeException("There was an error parsing ViewConfig. Cause: entity was not found.");
            }
            for (String fieldName : fieldNameSet) {
                if (entityHandler.containsField(fieldName)) {
                    if (viewFieldMap.containsKey(fieldName)) {
                        this.logger.error("There was an error parsing ViewConfig. Cause: view field {} is repeatable.", fieldName);
                        throw new RuntimeException("There was an error parsing ViewConfig. Cause: view field is repeatable.");
                    } else {
                        viewField = new Column();
                        viewField.setFieldName(fieldName);
                        viewField.setTableAliasName(tableAliasName);
                        viewField.setAliasName("");
                        viewFieldMap.put(fieldName, viewField);
                    }
                }
            }
        } else {
            String fieldName;
            String aliasName;
            for (ViewFieldConfig viewFieldConfig : viewFieldConfigs) {
                fieldName = viewFieldConfig.fieldName();
                aliasName = viewFieldConfig.aliasName();
                if (fieldNameSet.contains(aliasName)) {
                    if (viewFieldMap.containsKey(aliasName)) {
                        this.logger.error("There was an error parsing ViewConfig. Cause: view field {} is repeatable.", aliasName);
                        throw new RuntimeException("There was an error parsing ViewConfig. Cause: view field is repeatable.");
                    } else {
                        viewField = new Column();
                        viewField.setFieldName(fieldName);
                        viewField.setTableAliasName(tableAliasName);
                        viewField.setAliasName(aliasName);
                        viewFieldMap.put(aliasName, viewField);
                    }
                }
            }
        }
    }
}
