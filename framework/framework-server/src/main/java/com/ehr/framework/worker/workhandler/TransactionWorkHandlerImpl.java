package com.ehr.framework.worker.workhandler;

import com.ehr.framework.config.DefaultResponseFlagEnum;
import com.ehr.framework.context.TransactionSessionBeanLocalFactory;
import com.ehr.framework.exception.TranscationRollbackException;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.ResponseWriterManager;
import javax.naming.NamingException;
import org.slf4j.Logger;

/**
 * 事物处理类
 *
 * @author zoe
 */
public class TransactionWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final TransactionSessionBeanLocalFactory transactionSessionBeanLocalFactory;
    private final ResponseWriterManager responseWriterManager;

    public TransactionWorkHandlerImpl(final TransactionSessionBeanLocalFactory transactionSessionBeanLocalFactory, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.transactionSessionBeanLocalFactory = transactionSessionBeanLocalFactory;
        this.responseWriterManager = responseWriterManager;
    }

    @Override
    public void execute() {
        try {
            TransactionSessionBeanLocal transactionSessionBeanLocal = this.transactionSessionBeanLocalFactory.getTransactionSessionBeanLocal();
            transactionSessionBeanLocal.execute(this.nextWorkHandler);
        } catch (NamingException ex) {
            Logger logger = LogFactory.getFrameworkLogger();
            logger.error("create TransactionSessionBeanLocalFactory InitialContext failure", ex);
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            responseWriter.setFlag(DefaultResponseFlagEnum.EXCEPTION);
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
