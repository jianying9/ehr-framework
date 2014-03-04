package com.ehr.framework.worker;

import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.response.ResponseWriterImpl;
import com.ehr.framework.worker.workhandler.WritePageParameterManager;

/**
 * 输出对象的工具类
 * @author zoe
 */
public final class ResponseWriterManagerImpl implements ResponseWriterManager {

    private final ThreadLocal<ResponseWriter> threadLocal = new ThreadLocal<ResponseWriter>();
    private final WritePageParameterManager pageParameterManager;

    public ResponseWriterManagerImpl(final WritePageParameterManager pageParameterManager) {
        this.pageParameterManager = pageParameterManager;
    }

    @Override
    public ResponseWriter openThreadLocal() {
        ResponseWriter responseWriter = this.threadLocal.get();
        if (responseWriter == null) {
            responseWriter = new ResponseWriterImpl(this.pageParameterManager);
            this.threadLocal.set(responseWriter);
        }
        responseWriter.clear();
        return responseWriter;
    }

    @Override
    public void clear() {
        this.threadLocal.get().clear();
    }

    @Override
    public ResponseWriter getThreadLocal() {
        return threadLocal.get();
    }
}
