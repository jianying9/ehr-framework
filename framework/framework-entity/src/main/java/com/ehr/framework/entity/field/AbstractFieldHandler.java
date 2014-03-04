package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;

/**
 * filed处理抽象类
 * @author zoe
 */
public abstract class AbstractFieldHandler {

    protected final String name;
    protected final String exportName;
    protected final TypeHandler typeHandler;
    protected final FieldTypeEnum fieldType;
    protected final String dynamicDictionaryName;
    protected final String defaultValue;

    protected AbstractFieldHandler(final String name, final String exportName, final TypeHandler typeHandler, final FieldTypeEnum fieldType, final String dynamicDictionaryName, final String defaultValue) {
        this.name = name;
        this.exportName = exportName;
        this.typeHandler = typeHandler;
        this.fieldType = fieldType;
        this.dynamicDictionaryName = dynamicDictionaryName;
        if (defaultValue.isEmpty()) {
            this.defaultValue = this.typeHandler.getDefaultValue();
        } else {
            this.defaultValue = defaultValue;
        }
    }

    public final String getDefaultValue() {
        return this.defaultValue;
    }

    public final String getExportName() {
        return exportName;
    }

    public final FieldTypeEnum getFieldType() {
        return fieldType;
    }

    public final String getName() {
        return name;
    }

    public final TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public final String formatValue(final String value) {
        return this.typeHandler.formatValue(value);
    }

    public final String dynamicDictionaryName() {
        return this.dynamicDictionaryName;
    }
}
