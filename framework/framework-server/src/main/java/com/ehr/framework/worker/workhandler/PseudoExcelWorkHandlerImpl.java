package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.util.NumberUtils;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * excel伪实现输出处理类
 *
 * @author zoe
 */
public class PseudoExcelWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final String exportFileName;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;

    public PseudoExcelWorkHandlerImpl(final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final String exportFileName, final HttpServletRequestManager httpServletRequestManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.returnParameter = returnParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.exportFileName = exportFileName;
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
            int length = NumberUtils.getRandomIntegerValue(20);
            List<Map<String, String>> pseudoDataList = new ArrayList<Map<String, String>>(length);
            Map<String, String> pseudoData;
            FieldHandler fieldHandler;
            for (int index = 0; index < length; index++) {
                pseudoData = new HashMap<String, String>(this.returnParameter.length);
                for (String fieldName : this.returnParameter) {
                    fieldHandler = this.fieldHandlerMap.get(fieldName);
                    pseudoData.put(fieldName, fieldHandler.getRandomValue());
                }
                pseudoDataList.add(pseudoData);
            }
            responseWriter.setMapListData(pseudoDataList);
            responseWriter.success();
        } else {
            //执行真实实现
            this.nextWorkHandler.execute();
        }
    }
}
