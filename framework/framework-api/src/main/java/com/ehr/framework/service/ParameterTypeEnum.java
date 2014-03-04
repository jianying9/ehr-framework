package com.ehr.framework.service;

/**
 * 请求封装参数类型
 * @author zoe
 */
public enum ParameterTypeEnum {

    //无参数
    NO_PARAMETER,
    //单参数单值 String
    SIMPLE_PARAMETER,
    //单参数多值 String[]
    BATCH_PARAMETER,
    //多参数单值 Map<String, String>
    SIMPLE_MAP,
    //多参数多值 Map<String, String[]>
    BATCH_MAP,
    //文件 List<FileItem>
    FILE_ITEM_LIST,
    //excel值
    EXCEL_DATA;
}
