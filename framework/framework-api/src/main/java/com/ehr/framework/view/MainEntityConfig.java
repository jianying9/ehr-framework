package com.ehr.framework.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * entity annotation，用于描述主entity的信息
 * @author zoe
 */
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MainEntityConfig {

    String entityName();

    String aliasEntityName();

    ViewFieldConfig[] viewFieldConfigs() default {};
}
