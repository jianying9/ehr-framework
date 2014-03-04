package com.ehr.framework.context;

import com.ehr.framework.worker.workhandler.TransactionSessionBeanLocal;
import javax.naming.NamingException;

/**
 *
 * @author zoe
 */
public interface TransactionSessionBeanLocalFactory {

    public TransactionSessionBeanLocal getTransactionSessionBeanLocal() throws NamingException;
}
