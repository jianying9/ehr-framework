package com.ehr.framework.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * entity annotation，用于描述entity的信息
 * @author zoe
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EntityConfig {

    /**
     * 绑定数据源
     * @return 
     */
    String dataSourceName() default "Ehr";

    /**
     * 实体标识
     * @return 
     */
    String entityName();

    /**
     * 标识实体的key
     * @return 
     */
    String keyField();

    /**
     * 标识可以用于sql insert的字段，默认为该entity的所有field
     * @return 
     */
    String[] insertFields() default {};

    /**
     * 标识可以用于sql insert的字段,这些字典可能不属于entity的field
     * @return 
     */
    String[] insertExtendFields() default {};

    /**
     * 标识可以用于sql update的字段,默认为该entity的所有field
     * @return 
     */
    String[] updateFields() default {};

    /**
     * 标识可以用于sql update的字段,这些字典可能不属于entity的field
     * @return 
     */
    String[] updateExtendFields() default {};

    /**
     * 是否启用缓存
     * @return 
     */
    boolean useCache() default false;

    /**
     * 缓存最大数量
     * @return 
     */
    int maxElementsInMemory() default 1000;

    /**
     * 最长闲置时间
     * @return 
     */
    int timeToIdleSeconds() default 300;

    /**
     * 最长存活时间
     * @return 
     */
    int timeToLiveSeconds() default 3600;
}
