package com.ehr.framework.entity.field.type;

/**
 * 验证类对象工厂
 * @author zoe
 */
public interface TypeHandlerFactory {

    public TypeHandler getTypeHandler(final FieldTypeEnum filedTypeEnum);
}
