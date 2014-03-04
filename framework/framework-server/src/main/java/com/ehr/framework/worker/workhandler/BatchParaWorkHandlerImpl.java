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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

/**
 * 单个参数多值取值、验证处理类处理类
 *
 * @author zoe
 */
public class BatchParaWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final String paraName;
    private final FieldHandler fieldHandler;
    private final WriteBatchParaManager BatchParaManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final SessionManager sessionManager;
    private final AccessActionLogger accessActionLogger;

    public BatchParaWorkHandlerImpl(
            final String paraName,
            final FieldHandler fieldHandler,
            final WriteBatchParaManager BatchParaManager,
            final HttpServletRequestManager httpServletRequestManager,
            final HttpServletResponseManager httpServletResponseManager,
            final ResponseWriterManager responseWriterManager,
            final WorkHandler workHandler,
            final SessionManager sessionManager,
            final AccessActionLogger accessActionLogger) {
        this.nextWorkHandler = workHandler;
        this.paraName = paraName;
        this.fieldHandler = fieldHandler;
        this.BatchParaManager = BatchParaManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.sessionManager = sessionManager;
        this.accessActionLogger = accessActionLogger;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String[] parameterValues = request.getParameterValues(this.paraName);
        String errorMsg = "";
        String paraValue = "";
        if (parameterValues == null) {
            errorMsg = WorkHandler.NULL_MESSAGE;
        } else {
            for (int index = 0; index < parameterValues.length; index++) {
                paraValue = parameterValues[index];
                paraValue = HttpUtils.trim(paraValue);
                if (!paraValue.isEmpty()) {
                    errorMsg = this.fieldHandler.validate(paraValue);
                    if (errorMsg.isEmpty()) {
                        parameterValues[index] = paraValue;
                    } else {
                        break;
                    }
                } else {
                    errorMsg = WorkHandler.EMPTY_MESSAGE;
                    break;
                }
            }
        }
        if (errorMsg.isEmpty()) {
            this.insertAccessActionLog(parameterValues);
            this.BatchParaManager.openThreadLocal(parameterValues);
            this.nextWorkHandler.execute();
            this.BatchParaManager.closeThreadLocal();
        } else {
            errorMsg = this.paraName.concat(errorMsg);
            this.logger.debug("get parameter failure. Cause: {},value:{}", errorMsg, paraValue);
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            responseWriter.invalid();
            responseWriter.setInfo(errorMsg);
            HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
            responseWriter.writeJson(request, response);
        }
    }

    private void insertAccessActionLog(final String[] parameterValues) {
        UserInfoEntity userInfoEntity = this.sessionManager.getThreadLocal();
        if (userInfoEntity != null) {
            long companyId = userInfoEntity.getCompanyId();
            long userId = userInfoEntity.getUserId();
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            //访问日志
            String act = responseWriter.getAct();
            String parameter = JsonUtils.valuesNoDictionaryToJSON(parameterValues, this.fieldHandler);
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
