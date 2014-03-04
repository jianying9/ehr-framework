package com.ehr.framework.entity;

import com.ehr.framework.entity.field.FieldHandler;
import java.util.Collections;
import java.util.Map;

/**
 * 实体处理类
 * @author zoe
 */
public final class ExtendedEntityHandlerImpl implements ExtendedEntityHandler{

    private final Map<String, FieldHandler> fieldHandlerMap;

    ExtendedEntityHandlerImpl(final Map<String, FieldHandler> fieldHandlerMap) {
        this.fieldHandlerMap = fieldHandlerMap;
    }

    @Override
    public FieldHandler getFieldHandler(final String fieldName) {
        return this.fieldHandlerMap.get(fieldName);
    }

    @Override
    public boolean containsField(String fieldName) {
        return this.fieldHandlerMap.containsKey(fieldName);
    }
    
    @Override
    public Map<String, FieldHandler> getFieldHandlerMap() {
        return Collections.unmodifiableMap(this.fieldHandlerMap);
    }
}
