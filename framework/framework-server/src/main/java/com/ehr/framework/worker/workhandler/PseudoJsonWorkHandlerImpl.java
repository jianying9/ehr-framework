package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * JSON伪实现输出处理类
 *
 * @author zoe
 */
public class PseudoJsonWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;

    public PseudoJsonWorkHandlerImpl(final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final HttpServletRequestManager httpServletRequestManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.returnParameter = returnParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String noPseudo = request.getParameter("noPseudo");
        if (noPseudo == null) {
            //执行伪实现
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            //构造伪数据
            Map<String, String> pseudoData = new HashMap<String, String>(this.returnParameter.length);
            FieldHandler fieldHandler;
            for (String fieldName : this.returnParameter) {
                fieldHandler = this.fieldHandlerMap.get(fieldName);
                if (fieldHandler != null) {
                    pseudoData.put(fieldName, fieldHandler.getRandomValue());
                }
            }
            responseWriter.setMapData(pseudoData);
            responseWriter.success();
        } else {
            //执行真实实现
            this.nextWorkHandler.execute();
        }
    }
}
