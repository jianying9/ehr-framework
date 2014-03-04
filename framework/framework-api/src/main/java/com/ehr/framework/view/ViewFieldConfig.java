package com.ehr.framework.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * entity annotation，用于描述view的field信息描述
 * @author zoe
 */
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewFieldConfig {

    String fieldName();
    
    String aliasName();
    
}
