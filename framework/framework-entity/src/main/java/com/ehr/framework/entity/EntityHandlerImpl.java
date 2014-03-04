package com.ehr.framework.entity;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldHandler;
import java.util.Collections;
import java.util.Map;

/**
 * 实体处理类
 * @author zoe
 */
public final class EntityHandlerImpl<T extends Entity> implements EntityHandler<T> {

    private final Map<String, FieldHandler> fieldHandlerMap;

    EntityHandlerImpl(final Map<String, FieldHandler> fieldHandlerMap) {
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

    @Override
    public boolean validateFieldValue(final String fieldName, final String fieldValue) {
        boolean result = false;
        FieldHandler fieldHandler = this.getFieldHandler(fieldName);
        if (fieldHandler == null) {
            throw new RuntimeException(fieldName.concat(" can not find in this EntityHandler."));
        } else {
            if (fieldHandler.validate(fieldValue).isEmpty()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String formatFieldValue(final String fieldName, final String fieldValue) {
        String result = fieldValue;
        FieldHandler fieldHandler = this.getFieldHandler(fieldName);
        if (fieldHandler == null) {
            throw new RuntimeException(fieldName.concat(" can not find in this EntityHandler."));
        } else {
            result = fieldHandler.formatValue(fieldValue);
        }
        return result;
    }
}
