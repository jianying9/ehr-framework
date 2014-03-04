package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.PageParameterManager;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * JSON伪实现输出处理类
 *
 * @author zoe
 */
public class PseudoJsonPageWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final String[] returnParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final HttpServletRequestManager httpServletRequestManager;
    private final PageParameterManager pageParameterManager;
    private final ResponseWriterManager responseWriterManager;

    public PseudoJsonPageWorkHandlerImpl(final PageParameterManager pageParameterManager, final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final HttpServletRequestManager httpServletRequestManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.returnParameter = returnParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.pageParameterManager = pageParameterManager;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String noPseudo = request.getParameter("noPseudo");
        if (noPseudo == null) {
            //执行伪实现
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            int totalRows = 9999;
            PageExtendedEntity pageExtendedEntity = this.pageParameterManager.getThreadLocal();
            if (pageExtendedEntity.isPageDataExist(totalRows)) {
                //构造伪数据
                int pageRows = pageExtendedEntity.getPageRows();
                List<Map<String, String>> pseudoDataList = new ArrayList<Map<String, String>>(pageRows);
                Map<String, String> pseudoData;
                FieldHandler fieldHandler;
                for (int index = 0; index < pageRows; index++) {
                    pseudoData = new HashMap<String, String>(this.returnParameter.length);
                    for (String fieldName : this.returnParameter) {
                        fieldHandler = this.fieldHandlerMap.get(fieldName);
                        if (fieldHandler != null) {
                            pseudoData.put(fieldName, fieldHandler.getRandomValue());
                        }
                    }
                    pseudoDataList.add(pseudoData);
                }
                responseWriter.setMapListData(pseudoDataList);
                responseWriter.success();
            } else {
                responseWriter.nodata();
            }
        } else {
            //执行真实实现
            this.nextWorkHandler.execute();
        }
    }
}
