package com.ehr.framework.entity;

import com.ehr.framework.entity.field.FieldContextBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局信息构造类
 * @author zoe
 */
public class ExtendedEntityContextBuilderImpl implements ExtendedEntityContextBuilder {

    //extended entity处理集合
    private final Map<String, ExtendedEntityHandler> extendedEntityHandlerMap;
    private final FieldContextBuilder fieldContextBuilder;
    private final Map<String, String> existClassMap = new HashMap<String, String>(128);

    /**
     * 构造函数
     * @param properties 
     */
    public ExtendedEntityContextBuilderImpl(final FieldContextBuilder fieldContextBuilder) {
        this.fieldContextBuilder = fieldContextBuilder;
        this.extendedEntityHandlerMap = new HashMap<String, ExtendedEntityHandler>(16, 1);
    }

    @Override
    public final void putExtendedEntityHandler(final String extendedEntityName, final ExtendedEntityHandler extendedEntityHandler, String className) {
        if (this.extendedEntityHandlerMap.containsKey(extendedEntityName)) {
            String existClassName = this.existClassMap.get(extendedEntityName);
            if (existClassName == null) {
                existClassName = "NULL";
            }
            StringBuilder errBuilder = new StringBuilder(1024);
            errBuilder.append("There was an error putting extendedEntityHandler. Cause: extendedEntityName reduplicated : ")
                    .append(extendedEntityName).append("\n").append("exist class : ").append(existClassName)
                    .append("\n").append("this class : ").append(className);
            throw new RuntimeException(errBuilder.toString());
        }
        this.extendedEntityHandlerMap.put(extendedEntityName, extendedEntityHandler);
        this.existClassMap.put(extendedEntityName, className);
    }

    @Override
    public final ExtendedEntityHandler getExtendedEntityHandler(final String extendedEntityName) {
        return this.extendedEntityHandlerMap.get(extendedEntityName);
    }

    @Override
    public final Map<String, ExtendedEntityHandler> getExtendedEntityHandlerMap() {
        return Collections.unmodifiableMap(this.extendedEntityHandlerMap);
    }

    @Override
    public boolean assertExistExtendedEntity(String extendedEntityName) {
        return this.extendedEntityHandlerMap.containsKey(extendedEntityName);
    }

    @Override
    public FieldContextBuilder getFieldContextBuilder() {
        return this.fieldContextBuilder;
    }
}
