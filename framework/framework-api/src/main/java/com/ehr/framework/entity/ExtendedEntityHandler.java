package com.ehr.framework.entity;

import com.ehr.framework.entity.field.FieldHandler;
import java.util.Map;

/**
 * 实体处理类
 * @author zoe
 */
public interface ExtendedEntityHandler {

    public FieldHandler getFieldHandler(final String fieldName);

    public boolean containsField(String fieldName);
    
    public Map<String, FieldHandler> getFieldHandlerMap();
}
