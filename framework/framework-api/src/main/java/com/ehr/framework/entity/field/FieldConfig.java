package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.filter.FilterTypeEnum;
import com.ehr.framework.entity.field.type.FieldTypeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * entity filed annotation，用于描述entity中各个field的信息
 *
 * @author zoe
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FieldConfig {

    /**
     * field数据类型
     *
     * @return
     */
    public FieldTypeEnum type();

    /**
     * field描述
     *
     * @return
     */
    public String fieldDesc();

    /**
     * 该field是否是数据字典
     *
     * @return
     */
    public String dictionaryGroup() default "";

    /**
     * 动态数据字典类型
     *
     * @return
     */
    public String dynamicDictionaryName() default "";

    /**
     * 该field是否是数据字典array
     *
     * @return
     */
    public boolean dictionaryArray() default false;

    /**
     * 该field导出excel别名
     *
     * @return
     */
    public String exportName() default "";

    /**
     * 该field是否是jsonArray
     *
     * @return
     */
    public boolean jsonArray() default false;

    /**
     * 自定义jsonArray输出字符串，用于定义伪实现
     */
    public String jsonArrayTemp() default "[]";

    /**
     * 该field在http输出时过滤行为
     *
     * @return
     */
    public FilterTypeEnum[] filterTypes() default {FilterTypeEnum.ESCAPE, FilterTypeEnum.SECURITY};

    /**
     * 自定义默认值，将会覆盖字段类型的默认值
     */
    public String defaultValue() default "";
}
