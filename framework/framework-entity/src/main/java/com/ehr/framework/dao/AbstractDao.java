package com.ehr.framework.dao;

import com.ehr.framework.logger.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 * dynamic entity dao
 * @author zoe
 */
public abstract class AbstractDao<T extends Data> {

    protected final Logger logger = LogFactory.getFrameworkLogger();
    //实体class
    protected Class<T> clazz;
    //key
    protected String keyField;
    //插入集合
    protected String[] insertFields;
    //更新集合
    protected String[] updateFields;
    //数据源
    protected DataSource dataSource;
    //table name
    protected String tableName;
    //查询返回列模板
    protected String inquireSqlModel;

    AbstractDao() {
    }

    protected final void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected final void setInsertFields(String[] insertFields) {
        this.insertFields = insertFields;
    }

    protected final void setUpdateFields(String[] updateFields) {
        this.updateFields = updateFields;
    }

    protected final void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    protected final void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected final void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected final void setInquireSqlModel(String inquireSqlModel) {
        this.inquireSqlModel = inquireSqlModel;
    }

    /**
     * 解析map数据，实例化clazz
     * @param entityMap
     * @return 
     */
    protected final T newInstance(final Map<String, String> resultMap) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            logger.error("There was an error instancing  class {}.Cause: {}", clazz.getName(), e.getMessage());
            throw new RuntimeException("There was an error instancing class ".concat(clazz.getName()));
        }
        t.parseMap(resultMap);
        return t;
    }

    /**
     * 解析mapList为list<T>
     * @param mapList
     * @return 
     */
    protected final List<T> parseMapList(final List<Map<String, String>> mapList) {
        List<T> list;
        T t;
        if (mapList.isEmpty()) {
            list = new ArrayList<T>(0);
        } else {
            list = new ArrayList<T>(mapList.size());
            try {
                for (Map<String, String> map : mapList) {
                    t = clazz.newInstance();
                    t.parseMap(map);
                    list.add(t);
                }
            } catch (Exception e) {
                logger.error("There was an error instancing  class {}.Cause: {}", clazz.getName(), e.getMessage());
                throw new RuntimeException("There was an error instancing class ".concat(clazz.getName()));
            }
        }
        return list;
    }

    /**
     * 从entityMap中获取插入值
     * @param entityMap
     * @return 
     */
    protected final String[] getInsertValues(final Map<String, String> entityMap) {
        if (this.logger.isDebugEnabled()) {
            logger.debug("parse insert value:{}", entityMap.toString());
        }
        String[] values = new String[this.insertFields.length];
        String value;
        for (int index = 0; index < this.insertFields.length; index++) {
            value = entityMap.get(this.insertFields[index]);
            if (value == null) {
                throw new RuntimeException("There was an error parse insert values. Cause: could not find ".concat(this.insertFields[index]));
            }
            values[index] = value;
        }
        return values;
    }

    /**
     * 获取更新值
     * @param entityMap
     * @return 
     */
    protected final String[] getUpdateValues(final Map<String, String> entityMap) {
        List<String> valueList = new ArrayList<String>(this.updateFields.length + 1);
        for (String fieldName : this.updateFields) {
            if (entityMap.containsKey(fieldName)) {
                valueList.add(entityMap.get(fieldName));
            }
        }
        valueList.add(entityMap.get(this.keyField));
        return valueList.toArray(new String[valueList.size()]);
    }

    /**
     * 判断是否存在可更新列和主键
     * @param entityMap
     * @return 
     */
    protected final boolean assertCanUpdate(final Map<String, String> entityMap) {
        boolean result = false;
        if (entityMap.containsKey(this.keyField)) {
            for (String fieldName : this.updateFields) {
                if (entityMap.containsKey(fieldName)) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                this.logger.error("Invalid update values. Cause: could not find any update field");
            }
        } else {
            this.logger.error("Invalid update values. Cause: could not find keyField : {}", this.keyField);
        }
        return result;
    }
}
