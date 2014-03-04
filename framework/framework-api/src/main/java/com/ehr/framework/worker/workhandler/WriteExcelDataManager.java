package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.ExcelDataManager;

/**
 * 存放,读取,清除ExcelData参数的工具类
 * @author zoe
 */
public interface WriteExcelDataManager extends ExcelDataManager{
    
    public void openThreadLocal(ExcelData excelData);

    public void closeThreadLocal();
}
