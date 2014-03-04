package com.ehr.framework.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限信息配置
 *
 * @author zoe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrivilegeConfig {

    /**
     * 描述
     *
     * @return
     */
    public String description();

    /**
     * 所属权限组
     *
     * @return
     */
    public String parent() default "";

    /**
     * 类型
     *
     * @return
     */
    public String type() default PrivilegeConfigType.TOP;

    /**
     * 是否是负责人权限
     *
     * @return
     */
    public boolean manager() default false;

    /**
     * 权限类型
     *
     * @return
     */
    public int versionType() default VersionType.ALL;
}
