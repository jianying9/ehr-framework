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
public @interface ActionConfig {

    /**
     * 模块名称
     *
     * @return
     */
    public String moduleName();
    
    /**
     * 权限类型
     *
     * @return
     */
    public int versionType() default VersionType.EXTEND;
    
    /**
     * 自定义权限为空时，是否继承模块配置
     * @return 
     */
    public boolean customPrivilegeExtend() default true;

    /**
     * 自定义权限名称
     *
     * @return
     */
    public String[] customPrivilegeName() default {};

    /**
     * 是否需要管理员权限
     *
     * @return
     */
    public boolean requireAdmin() default false;

    /**
     * 简要描述
     *
     * @return
     */
    public String shortDescription();

    /**
     * 伪实现标志,影响该接口
     *
     * @return
     */
    public boolean pseudo() default false;
}
