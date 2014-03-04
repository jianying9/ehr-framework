package com.ehr.framework.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块信息配置
 *
 * @author zoe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModuleConfig {

    /**
     * 描述
     *
     * @return
     */
    public String description();

    /**
     * 上级名称
     *
     * @return
     */
    public String parentName() default "";

    /**
     * 是否要求管理员
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
     * 版本
     *
     * @return
     */
    public int versionType();

    /**
     * 伪实现标志,影响该模块对印的接口
     *
     * @return
     */
    public boolean pseudo() default false;
}
