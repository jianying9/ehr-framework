package com.ehr.framework.entity;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldContextBuilder;
import java.util.Map;

/**
 *
 * @author zoe
 */
public interface EntityContextBuilder<T extends Entity> {

    public void putEntityHandler(final String entityName, final EntityHandler<T> entityHandler, String className);

    public EntityHandler<T> getEntityHandler(final String entityName);

    public Map<String, EntityHandler<T>> getEntityHandlerMap();

    public boolean assertExistEntity(final String entityName);

    public FieldContextBuilder getFieldContextBuilder();
}
