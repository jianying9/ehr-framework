package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * excel输出处理类
 * @author zoe
 */
public class ExcelWriterWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final String exportFileName;
    private final HttpServletRequestManager httpServletRequestManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final ResponseWriterManager responseWriterManager;

    public ExcelWriterWorkHandlerImpl(final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final String exportFileName, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.returnParameter = returnParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.exportFileName = exportFileName;
        this.httpServletRequestManager = httpServletRequestManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.responseWriterManager = responseWriterManager;
    }

    @Override
    public void execute() {
        if (this.nextWorkHandler != null) {
            this.nextWorkHandler.execute();
        }
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
        ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
        responseWriter.writeExcel(request, response, this.fieldHandlerMap, this.returnParameter, this.exportFileName);
    }
}
