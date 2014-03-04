package com.ehr.framework.entity.field.type;

import java.util.EnumMap;
import java.util.Map;

/**
 * 验证类对象工厂
 * @author zoe
 */
public final class TypeHandlerFactoryImpl implements TypeHandlerFactory {

    public TypeHandlerFactoryImpl() {
        this.typeHandlerMap.put(FieldTypeEnum.TINY_INT_SIGNED, new TinyIntSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.SMALL_INT_SIGNED, new SmallIntSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.MEDIUM_INT_SIGNED, new MediumIntSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.INT_SIGNED, new IntSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.BIG_INT_SIGNED, new BigIntSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.DOUBLE_SIGNED, new DoubleSignedTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.DATETIME, new DateTimeTypeHanderImpl());
        this.typeHandlerMap.put(FieldTypeEnum.DATETIME_OR_EMPTY, new DateTimeOrEmptyTypeHanderImpl());
        this.typeHandlerMap.put(FieldTypeEnum.DATE, new DateTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.DATE_OR_EMPTY, new DateOrEmptyTypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR8, new Char8TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR16, new Char16TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR24, new Char24TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR32, new Char32TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR36, new Char36TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR64, new Char64TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR128, new Char128TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR256, new Char256TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR512, new Char512TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR1024, new Char1024TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR2048, new Char2048TypeHandlerImpl());
        this.typeHandlerMap.put(FieldTypeEnum.CHAR4096, new Char4096TypeHandlerImpl());
    }
    private final Map<FieldTypeEnum, TypeHandler> typeHandlerMap = new EnumMap<FieldTypeEnum, TypeHandler>(FieldTypeEnum.class);

    @Override
    public TypeHandler getTypeHandler(final FieldTypeEnum filedTypeEnum) {
        return this.typeHandlerMap.get(filedTypeEnum);
    }
}
