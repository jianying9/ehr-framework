package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.SimpleMapManager;
import java.util.Map;

/**
 * 在线程中存放,读取,清除SimpleMap参数的管理类
 * @author zoe
 */
public interface WriteSimpleMapManager extends SimpleMapManager{
    
    public void openThreadLocal(Map<String, String> parameterMap);

    public void closeThreadLocal();
}
