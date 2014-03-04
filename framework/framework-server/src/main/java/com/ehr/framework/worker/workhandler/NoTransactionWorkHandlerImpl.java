package com.ehr.framework.worker.workhandler;

import com.ehr.framework.config.DefaultResponseFlagEnum;
import com.ehr.framework.exception.TranscationRollbackException;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.ResponseWriterManager;
import org.slf4j.Logger;

/**
 * 无事物处理类
 *
 * @author zoe
 */
public class NoTransactionWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final ResponseWriterManager responseWriterManager;

    public NoTransactionWorkHandlerImpl(final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.responseWriterManager = responseWriterManager;
    }

    @Override
    public void execute() {
        try {
            this.nextWorkHandler.execute();
        } catch (RuntimeException re) {
            Throwable t = re.getCause();
            if (t == null) {
                t = re;
            }
            Logger logger = LogFactory.getFrameworkLogger();
            logger.error("ehr", t);
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            if (TranscationRollbackException.class.isInstance(t)) {
                TranscationRollbackException te = (TranscationRollbackException) t;
                responseWriter.setFlag(te.getFlagName());
                responseWriter.setInfo(te.getInfo());
            } else {
                responseWriter.setFlag(DefaultResponseFlagEnum.EXCEPTION);
            }
        }
    }
}
