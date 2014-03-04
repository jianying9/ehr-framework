package com.ehr.framework.worker.workhandler;

/**
 * 存放,读取,清除ExcelData参数的工具类
 * @author zoe
 */
public final class ExcelDataManagerImpl implements WriteExcelDataManager {

    private final ThreadLocal<ExcelData> threadLocal = new ThreadLocal<ExcelData>();

    @Override
    public void openThreadLocal(ExcelData excelData) {
        this.threadLocal.set(excelData);
    }

    @Override
    public void closeThreadLocal() {
        this.threadLocal.remove();
    }

    @Override
    public ExcelData getThreadLocal() {
        return this.threadLocal.get();
    }
}
