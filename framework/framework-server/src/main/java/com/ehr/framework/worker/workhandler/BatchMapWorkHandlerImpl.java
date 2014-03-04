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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

/**
 * 多参数多值取值、验证处理类处理类
 *
 * @author zoe
 */
public class BatchMapWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final String[] importantParameter;
    private final String[] minorParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final WriteBatchMapManager BatchMapManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final SessionManager sessionManager;
    private final AccessActionLogger accessActionLogger;

    public BatchMapWorkHandlerImpl(final String[] importantParameter,
            final String[] minorParameter,
            final Map<String, FieldHandler> fieldHandlerMap,
            final WriteBatchMapManager BatchMapManager,
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
        this.BatchMapManager = BatchMapManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.sessionManager = sessionManager;
        this.accessActionLogger = accessActionLogger;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(importantParameter.length + minorParameter.length, 1);
        String[] parameterValues;
        String errorMsg = "";
        String paraValue = "";
        String errorParaName = "";
        FieldHandler fieldHandler;
        for (String parameter : this.importantParameter) {
            parameterValues = request.getParameterValues(parameter);
            if (parameterValues == null) {
                errorMsg = WorkHandler.NULL_MESSAGE;
                errorParaName = parameter;
            } else {
                fieldHandler = this.fieldHandlerMap.get(parameter);
                for (int index = 0; index < parameterValues.length; index++) {
                    paraValue = parameterValues[index];
                    paraValue = HttpUtils.trim(paraValue);
                    if (!paraValue.isEmpty()) {
                        errorMsg = fieldHandler.validate(paraValue);
                        if (errorMsg.isEmpty()) {
                            parameterValues[index] = paraValue;
                        } else {
                            errorParaName = parameter;
                            break;
                        }
                    } else {
                        errorMsg = WorkHandler.EMPTY_MESSAGE;
                        errorParaName = parameter;
                        break;
                    }
                }
            }
            if (errorMsg.isEmpty()) {
                parameterMap.put(parameter, parameterValues);
            } else {
                break;
            }
        }
        if (errorMsg.isEmpty()) {
            for (String parameter : this.minorParameter) {
                parameterValues = request.getParameterValues(parameter);
                if (parameterValues != null) {
                    fieldHandler = this.fieldHandlerMap.get(parameter);
                    for (int index = 0; index < parameterValues.length; index++) {
                        paraValue = parameterValues[index];
                        paraValue = HttpUtils.trim(paraValue);
                        if (paraValue.isEmpty()) {
                            paraValue = fieldHandler.getDefaultValue();
                            parameterValues[index] = paraValue;
                        } else {
                            errorMsg = fieldHandler.validate(paraValue);
                            if (errorMsg.isEmpty()) {
                                parameterValues[index] = paraValue;
                            } else {
                                errorParaName = parameter;
                                break;
                            }
                        }
                    }
                    if (errorMsg.isEmpty()) {
                        parameterMap.put(parameter, parameterValues);
                    } else {
                        break;
                    }
                }
            }
        }
        if (errorMsg.isEmpty()) {
            this.insertAccessActionLog(parameterMap);
            this.BatchMapManager.openThreadLocal(parameterMap);
            this.nextWorkHandler.execute();
            this.BatchMapManager.closeThreadLocal();
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

    private void insertAccessActionLog(final Map<String, String[]> parameterMap) {
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
            int maxLength = 0;
            String[] values;
            for (String name : parameters) {
                values = parameterMap.get(name);
                if (values != null && values.length > maxLength) {
                    maxLength = values.length;
                }
            }
            List<Map<String, String>> parameterMapList = new ArrayList<Map<String, String>>(maxLength);
            Map<String, String> paraMap;
            String value;
            for (int index = 0; index < maxLength; index++) {
                paraMap = new HashMap<String, String>(parameters.length, 1);
                for (String name : parameters) {
                    values = parameterMap.get(name);
                    if (values != null && values.length > index) {
                        value = values[index];
                    } else {
                        value = "";
                    }
                    paraMap.put(name, value);
                }
                parameterMapList.add(paraMap);
            }
            String parameter = JsonUtils.mapListNoDictionaryToJSON(parameterMapList, parameters, this.fieldHandlerMap);
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
