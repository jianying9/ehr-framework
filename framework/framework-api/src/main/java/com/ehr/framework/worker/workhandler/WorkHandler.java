package com.ehr.framework.worker.workhandler;

/**
 * 工作处理类
 * @author zoe
 */
public interface WorkHandler {

    public String NULL_MESSAGE = " is NULL";
    public String EMPTY_MESSAGE = " is empty";

    public void execute();
}
