package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.worker.HttpServletRequestManager;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 分页参数验证处理类处理类
 *
 * @author zoe
 */
public class PageParameterWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final WritePageParameterManager pageParameterManager;
    private final HttpServletRequestManager httpServletRequestManager;

    public PageParameterWorkHandlerImpl(final Map<String, FieldHandler> fieldHandlerMap, final WritePageParameterManager pageParameterManager, final HttpServletRequestManager httpServletRequestManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.fieldHandlerMap = fieldHandlerMap;
        this.pageParameterManager = pageParameterManager;
        this.httpServletRequestManager = httpServletRequestManager;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String pageIndex = this.getValue(request, "pageIndex");
        String pageRows = this.getValue(request, "pageRows");
        PageExtendedEntity pageExtendedEntity = new PageExtendedEntityImpl();
        pageExtendedEntity.setPageIndex(pageIndex);
        pageExtendedEntity.setPageRows(pageRows);
        this.pageParameterManager.openThreadLocal(pageExtendedEntity);
        this.nextWorkHandler.execute();
        this.pageParameterManager.closeThreadLocal();
    }

    private String getValue(final HttpServletRequest request, final String fieldName) {
        String result = "0";
        String value = request.getParameter(fieldName);
        if (value != null) {
            value = HttpUtils.trim(value);
            if (!value.isEmpty()) {
                FieldHandler fieldHandler = this.fieldHandlerMap.get(fieldName);
                String errMsg = fieldHandler.validate(value);
                if (errMsg.isEmpty()) {
                    result = value;
                }
            }
        }
        return result;
    }
}
