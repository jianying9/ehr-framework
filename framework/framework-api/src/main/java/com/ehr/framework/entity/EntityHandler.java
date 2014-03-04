package com.ehr.framework.entity;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldHandler;
import java.util.Map;

/**
 * 实体处理类
 * @author zoe
 */
public interface EntityHandler<T extends Entity> {

    public FieldHandler getFieldHandler(final String fieldName);

    public boolean containsField(String fieldName);

    public Map<String, FieldHandler> getFieldHandlerMap();

    public boolean validateFieldValue(final String fieldName, final String fieldValue);

    public String formatFieldValue(final String fieldName, final String fieldValue);
}
