package com.ehr.framework.config;

/**
 *
 * @author zoe
 */
public enum DefaultResponseFlagEnum implements ResponseFlagType {

    //成功
    SUCCESS,
    //失败
    FAILURE,
    //未登录
    UNLOGIN,
    //非法数据
    INVALID,
    //无权限
    DENIED,
    //超时
    TIMEOUT,
    //没有文件
    NO_FILE,
    //没有数据
    NO_DATA,
    //异常
    EXCEPTION;

    @Override
    public String getFlagName() {
        return this.name();
    }
}
