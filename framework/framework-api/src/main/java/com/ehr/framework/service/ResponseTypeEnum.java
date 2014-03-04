package com.ehr.framework.service;

/**
 * 请求封装参数类型
 * @author zoe
 */
public enum ResponseTypeEnum {

    //无输出
    NO,
    //json
    JSON,
    //分页查询返回数据
    JSON_PAGE,
    //excel导出
    EXCEL,
    //导出EXCEL模板
    EXCEL_MODEL;
}
