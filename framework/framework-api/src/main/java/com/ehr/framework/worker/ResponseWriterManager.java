package com.ehr.framework.worker;

import com.ehr.framework.response.ResponseWriter;

/**
 * 输出对象的工具类
 * @author zoe
 */
public interface ResponseWriterManager {

    public ResponseWriter openThreadLocal();

    public void clear();

    public ResponseWriter getThreadLocal();
}
