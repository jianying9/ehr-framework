package com.ehr.framework.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * extended entity annotation，用于描述扩展字段的信息
 * @author zoe
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExtendedEntityConfig {

    /**
     * 扩展实体标识
     * @return 
     */
    String extendedEntityName();
}
