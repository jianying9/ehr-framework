package com.ehr.framework.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导航信息配置
 *
 * @author zoe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NavigationConfig {

    /**
     * 版本
     *
     * @return
     */
    public int versionType() default VersionType.NOT_ACTIVATE;

    /**
     * 是否需要管理员
     *
     * @return
     */
    public boolean requireAdmin() default false;

    /**
     * 自定义权限名称
     *
     * @return
     */
    public String[] customPrivilegeName() default {};

    /**
     * 简要描述
     *
     * @return
     */
    public String description();

    /**
     * 上级导航
     *
     * @return
     */
    public String parentName() default "";
    
    /**
     * 是否是负责人权限
     *
     * @return
     */
    public boolean manager() default false;
}
