package com.ehr.framework.context;

import com.ehr.framework.worker.workhandler.ExcelData;

/**
 * 存放,读取,清除ExcelData参数的工具类
 * @author zoe
 */
public interface ExcelDataManager {

    public ExcelData getThreadLocal();
}
