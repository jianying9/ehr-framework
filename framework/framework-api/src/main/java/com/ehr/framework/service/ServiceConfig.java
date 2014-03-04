package com.ehr.framework.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务信息配置
 *
 * @author zoe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceConfig {

    /**
     * 服务标志
     *
     * @return ActionEnum
     */
    public String actionName();

    /**
     * 重要的参数
     *
     * @return String[]
     */
    public String[] importantParameter() default {};

    /**
     * 次要的参数
     *
     * @return String[]
     */
    public String[] minorParameter() default {};

    /**
     * 次要参数处理方法 KEEP_EMPEY-保留空字符串 DISCARD_EMPTY-丢弃空字符串
     * DEFAULT_REPLACE_NULL-用缺省值填充NULL
     *
     * @return
     */
    public MinorHandlerTypeEnum minorHandlerTypeEnum() default MinorHandlerTypeEnum.KEEP_EMPTY;

    /**
     * 返回的参数
     *
     * @return String[]
     */
    public String[] returnParameter() default {};

    /**
     * 验证参数来源，实体有顺序，如果有重复取最先出现的EntityEnum中的field信息
     *
     * @return Class<?>[]
     */
    public String[] entityNames() default {};

    /**
     * 验证参数来源，实体有顺序，如果有重复取最先出现的ExtendedEntityEnum中的field信息
     *
     * @return
     */
    public String[] extendedEntityNames() default {};

    /**
     * 参数管理类 单参数单值,用String来传递---SIMPLE_PARAMETER
     * 单参数多值,用String[]来传递---BATCH_PARAMETER
     * 多参数单值,用Map<String,String>来传递---SIMPLE_MAP
     * 多参数多值,用Map<String,String[]>来传递---BATCH_MAP 不获取任何参数---NO_PARAMETER
     * 多文件上传--List<FileItem>--FILE_ITEM_LIST excel数据导入--List<Map<String,
     * String>>--MAP_LIST
     *
     * @return
     */
    public ParameterTypeEnum parameterTypeEnum() default ParameterTypeEnum.NO_PARAMETER;

    /**
     * 响应类型 JSON JSON_PAGE EXCEL
     *
     * @return
     */
    public ResponseTypeEnum responseTypeEnum() default ResponseTypeEnum.JSON;

    /**
     * 事务类型 需要事务控制--true 不需要事务控制--false
     *
     * @return
     */
    public boolean requireTransaction() default true;

    /**
     * 登录验证 获取登录信息---true 不获取登录信息---false
     *
     * @return
     */
    @Deprecated
    public boolean requireHttpSession() default true;

    /**
     * 导出文件名称
     *
     * @return String
     */
    public String exportFileName() default "91ehr";

    /**
     * 需要缓存数据的flag
     *
     * @return
     */
    public String[] cacheFlag() default {};

    /**
     * 描述
     *
     * @return
     */
    public String getDescription();
}
