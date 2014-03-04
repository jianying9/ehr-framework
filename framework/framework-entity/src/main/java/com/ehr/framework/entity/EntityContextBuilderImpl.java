package com.ehr.framework.entity;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldContextBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局信息构造类
 * @author zoe
 */
public class EntityContextBuilderImpl<T extends Entity> implements EntityContextBuilder<T> {

    //缓存管理对象
    private final FieldContextBuilder fieldContextBuilder;

    //entity处理类集合
    private final Map<String, EntityHandler<T>> entityHandlerMap;
    
    private final Map<String, String> existClassMap = new HashMap<String, String>(128);

    @Override
    public final void putEntityHandler(final String entityName, final EntityHandler<T> entityHandler, String className) {
        if (this.entityHandlerMap.containsKey(entityName)) {
            String existClassName = this.existClassMap.get(entityName);
            if (existClassName == null) {
                existClassName = "NULL";
            }
            StringBuilder errBuilder = new StringBuilder(1024);
            errBuilder.append("There was an error putting entityHandler. Cause: entityName reduplicated : ")
                    .append(entityName).append("\n").append("exist class : ").append(existClassName)
                    .append("\n").append("this class : ").append(className);
            throw new RuntimeException(errBuilder.toString());
        }
        this.entityHandlerMap.put(entityName, entityHandler);
        this.existClassMap.put(entityName, className);
    }

    @Override
    public final EntityHandler<T> getEntityHandler(final String entityName) {
        return this.entityHandlerMap.get(entityName);
    }

    @Override
    public Map<String, EntityHandler<T>> getEntityHandlerMap() {
        return Collections.unmodifiableMap(this.entityHandlerMap);
    }

    /**
     * 构造函数
     * @param properties 
     */
    public EntityContextBuilderImpl(final FieldContextBuilder fieldContextBuilder) {
        this.entityHandlerMap = new HashMap<String, EntityHandler<T>>(64, 1);
        this.fieldContextBuilder = fieldContextBuilder;
    }


    @Override
    public boolean assertExistEntity(String entityName) {
        return this.entityHandlerMap.containsKey(entityName);
    }

    @Override
    public FieldContextBuilder getFieldContextBuilder() {
        return this.fieldContextBuilder;
    }
}
