package com.ehr.framework.worker;

import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.workhandler.WorkHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务工作对象接口
 *
 * @author zoe
 */
public final class ServiceWorkerImpl implements ServiceWorker {

    private final WorkHandler nextWorkHandler;
    private final String actionName;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final HttpServletResponseManager httpServletResponseManager;

    public ServiceWorkerImpl(final String actionName, final WorkHandler workHandler, final ResponseWriterManager responseWriterManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager) {
        this.nextWorkHandler = workHandler;
        this.actionName = actionName;
        this.responseWriterManager = responseWriterManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.httpServletResponseManager = httpServletResponseManager;
    }

    @Override
    public void doWork(HttpServletRequest request, HttpServletResponse response) {
        this.httpServletRequestManager.openThreadLocal(request);
        this.httpServletResponseManager.openThreadLocal(response);
        ResponseWriter responseWriter = this.responseWriterManager.openThreadLocal();
        responseWriter.setAct(this.actionName);
        String ui = request.getParameter("ui");
        if (ui == null) {
            ui = "-1";
        }
        responseWriter.setUi(ui);
        this.nextWorkHandler.execute();
        this.httpServletRequestManager.closeThreadLocal();
        this.httpServletResponseManager.closeThreadLocal();
    }
}
