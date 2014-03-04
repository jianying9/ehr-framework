package com.ehr.framework.entity.field.type;

/**
 * entity filed type
 * @author zoe
 */
public enum FieldTypeEnum {
    //TinyIntSigned [-128,127]
    TINY_INT_SIGNED,
    //SmallIntSigned [-32768,32767]
    SMALL_INT_SIGNED,
    //MediumIntSigned [-8388608,8388607]
    MEDIUM_INT_SIGNED,
    //IntSigned [-2147483648,2147483647]
    INT_SIGNED,
    //BigIntSigned [-9223372036854775808,9223372036854775807]
    BIG_INT_SIGNED,
    //Double [-1.7976931348623157×10+308, -4.94065645841246544×10-324]
    DOUBLE_SIGNED,
    //DateTime {YYYY-MM-DD HH:MM,YYYY-MM-DD HH:MM:SS}
    DATETIME,
    //DateTime or empty {YYYY-MM-DD HH:MM,YYYY-MM-DD HH:MM:SS}
    DATETIME_OR_EMPTY,
    //Date {YYYY-MM-DD,YYYY-m-d}
    DATE,
    //Date or empty {YYYY-MM-DD,YYYY-m-d}
    DATE_OR_EMPTY,
    //Char8 max length 8,可以包含特殊符号
    CHAR8,
    //Char16 max length 16,可以包含特殊符号
    CHAR16,
    //Char24 max length 24,可以包含特殊符号
    CHAR24,
    //Char32 max length 32,可以包含特殊符号
    CHAR32,
    //Char36 max length 36,可以包含特殊符号
    CHAR36,
    //Char64 max length 64,可以包含特殊符号
    CHAR64,
    //Char128 max length 128,可以包含特殊符号
    CHAR128,
    //Char256 max length 256,可以包含特殊符号
    CHAR256,
    //Char512 max length 512,可以包含特殊符号
    CHAR512,
    //Char1024 max length 1024,可以包含特殊符号
    CHAR1024,
    //Char2048 max length 2048,可以包含特殊符号
    CHAR2048,
    //Char4096 max length 4096,可以包含特殊符号
    CHAR4096;
}
