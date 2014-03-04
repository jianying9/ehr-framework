package com.ehr.framework.entity;

import com.ehr.framework.entity.field.FieldContextBuilder;
import java.util.Map;

/**
 *
 * @author zoe
 */
public interface ExtendedEntityContextBuilder {

    public ExtendedEntityHandler getExtendedEntityHandler(final String extendedEntityName);

    public Map<String, ExtendedEntityHandler> getExtendedEntityHandlerMap();

    public void putExtendedEntityHandler(final String extendedEntityName, final ExtendedEntityHandler extendedEntityHandler, String className);

    public boolean assertExistExtendedEntity(final String extendedEntityName);
    
    public FieldContextBuilder getFieldContextBuilder();
}
