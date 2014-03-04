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
 * JSON输出处理类
 * @author zoe
 */
public class JsonWriterWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final HttpServletRequestManager httpServletRequestManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final ResponseWriterManager responseWriterManager;

    public JsonWriterWorkHandlerImpl(final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.returnParameter = returnParameter;
        this.fieldHandlerMap = fieldHandlerMap;
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
        String[] returnNames = this.returnParameter;
        String columnName = request.getParameter("returnNames");
        if (columnName != null) {
            returnNames = columnName.split(",");
        }
        responseWriter.writeJson(request, response, this.fieldHandlerMap, returnNames);
    }
}
