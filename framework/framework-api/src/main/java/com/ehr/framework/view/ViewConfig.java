package com.ehr.framework.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dynamic entity annotation，用于描述dynamic entity的信息
 * @author zoe
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewConfig {

    /**
     * 数据源标识
     * @return 
     */
    String dataSourceName() default "Ehr";

    /**
     * 视图标识
     * @return 
     */
    String viewName();

    MainEntityConfig mainEntity();

    RelationalEntityConfig[] relationalEntitys();
}
