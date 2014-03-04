package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.SessionManager;
import com.ehr.framework.logger.AccessActionLog;
import com.ehr.framework.logger.AccessActionLogger;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.util.TimeUtils;
import com.ehr.framework.worker.ResponseWriterManager;

/**
 * 无参数处理类
 *
 * @author zoe
 */
public class NoParaWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final ResponseWriterManager responseWriterManager;
    private final SessionManager sessionManager;
    private final AccessActionLogger accessActionLogger;

    public NoParaWorkHandlerImpl(
        final ResponseWriterManager responseWriterManager,
        final WorkHandler workHandler,
        final SessionManager sessionManager,
        final AccessActionLogger accessActionLogger) {
        this.nextWorkHandler = workHandler;
        this.responseWriterManager = responseWriterManager;
        this.sessionManager = sessionManager;
        this.accessActionLogger = accessActionLogger;
    }

    @Override
    public void execute() {
        this.insertAccessActionLog();
        this.nextWorkHandler.execute();
    }

    private void insertAccessActionLog() {
        UserInfoEntity userInfoEntity = this.sessionManager.getThreadLocal();
        if (userInfoEntity != null) {
            long companyId = userInfoEntity.getCompanyId();
            long userId = userInfoEntity.getUserId();
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            //访问日志
            String act = responseWriter.getAct();
            String parameter = "";
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
