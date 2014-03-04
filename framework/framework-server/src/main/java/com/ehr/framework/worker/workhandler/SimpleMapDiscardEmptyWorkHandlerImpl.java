package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.SessionManager;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.logger.AccessActionLog;
import com.ehr.framework.logger.AccessActionLogger;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.util.JsonUtils;
import com.ehr.framework.util.TimeUtils;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

/**
 * 多个参数单值处理类,次要参数丢弃空字符
 *
 * @author zoe
 */
public class SimpleMapDiscardEmptyWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final String[] importantParameter;
    private final String[] minorParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final WriteSimpleMapManager simpleMapManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final SessionManager sessionManager;
    private final AccessActionLogger accessActionLogger;

    public SimpleMapDiscardEmptyWorkHandlerImpl(
            final String[] importantParameter,
            final String[] minorParameter,
            final Map<String, FieldHandler> fieldHandlerMap,
            final WriteSimpleMapManager simpleMapManager,
            final HttpServletRequestManager httpServletRequestManager,
            final HttpServletResponseManager httpServletResponseManager,
            final ResponseWriterManager responseWriterManager,
            final WorkHandler workHandler,
            final SessionManager sessionManager,
            final AccessActionLogger accessActionLogger) {
        this.nextWorkHandler = workHandler;
        this.importantParameter = importantParameter;
        this.minorParameter = minorParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.simpleMapManager = simpleMapManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.sessionManager = sessionManager;
        this.accessActionLogger = accessActionLogger;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        Map<String, String> parameterMap = new HashMap<String, String>(importantParameter.length + minorParameter.length, 1);
        String paraValue = "";
        String errorParaName = "";
        String errorMsg = "";
        FieldHandler fieldHandler;
        for (String parameter : this.importantParameter) {
            paraValue = request.getParameter(parameter);
            if (paraValue == null) {
                errorMsg = WorkHandler.NULL_MESSAGE;
                errorParaName = parameter;
                break;
            }
            paraValue = HttpUtils.trim(paraValue);
            if (paraValue.isEmpty()) {
                errorMsg = WorkHandler.EMPTY_MESSAGE;
                errorParaName = parameter;
                break;
            }
            fieldHandler = this.fieldHandlerMap.get(parameter);
            errorMsg = fieldHandler.validate(paraValue);
            if (errorMsg.isEmpty()) {
                parameterMap.put(parameter, paraValue);
            } else {
                errorParaName = parameter;
                break;
            }
        }
        if (errorMsg.isEmpty()) {
            for (String parameter : this.minorParameter) {
                paraValue = request.getParameter(parameter);
                if (paraValue != null) {
                    paraValue = HttpUtils.trim(paraValue);
                    if (!paraValue.isEmpty()) {
                        fieldHandler = this.fieldHandlerMap.get(parameter);
                        errorMsg = fieldHandler.validate(paraValue);
                        if (errorMsg.isEmpty()) {
                            parameterMap.put(parameter, paraValue);
                        } else {
                            errorParaName = parameter;
                            break;
                        }
                    }
                }
            }
        }
        if (errorMsg.isEmpty()) {
            this.insertAccessActionLog(parameterMap);
            this.simpleMapManager.openThreadLocal(parameterMap);
            this.nextWorkHandler.execute();
            this.simpleMapManager.closeThreadLocal();
        } else {
            errorMsg = errorParaName.concat(errorMsg);
            this.logger.debug("get parameter failure. Cause: {},value:{}", errorMsg, paraValue);
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            responseWriter.invalid();
            responseWriter.setInfo(errorMsg);
            HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
            responseWriter.writeJson(request, response);
        }
    }

    private void insertAccessActionLog(final Map<String, String> parameterMap) {
        UserInfoEntity userInfoEntity = this.sessionManager.getThreadLocal();
        if (userInfoEntity != null) {
            long companyId = userInfoEntity.getCompanyId();
            long userId = userInfoEntity.getUserId();
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            //访问日志
            String act = responseWriter.getAct();
            String[] parameters = new String[this.importantParameter.length + this.minorParameter.length];
            System.arraycopy(this.importantParameter, 0, parameters, 0, this.importantParameter.length);
            if (this.minorParameter.length > 0) {
                System.arraycopy(this.minorParameter, 0, parameters, this.importantParameter.length, this.minorParameter.length);
            }
            String parameter = JsonUtils.mapNoDictionaryToJSON(parameterMap, parameters, this.fieldHandlerMap);
            AccessActionLog accessActionLog = new AccessActionLog();
            accessActionLog.setAction(act);
            accessActionLog.setCompanyId(companyId);
            accessActionLog.setUserId(userId);
            accessActionLog.setParameter(parameter);
            accessActionLog.setDateTime(TimeUtils.getDateFotmatYYMMDDHHmmSS());
            this.accessActionLogger.insertAccessActionLog(accessActionLog);
        }
    }
}
