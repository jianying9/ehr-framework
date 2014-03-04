package com.ehr.framework.entity.field.type;

import com.ehr.framework.entity.field.FieldTypeClassEnum;

/**
 * 字符类型验证抽象类
 * @author zoe
 */
public abstract class AbstractTypeHandler {

    protected abstract String getErrorMessage();

    public abstract String getDefaultValue();

    public abstract FieldTypeClassEnum getFieldTypeClassEnum();
}
