package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.BatchMapManager;
import java.util.Map;

/**
 * 存放,读取,清除BatchMap参数的工具类
 * @author zoe
 */
public interface WriteBatchMapManager extends BatchMapManager {

    public void openThreadLocal(Map<String, String[]> parameterMap);

    public void closeThreadLocal();
}
