package com.ehr.framework.worker.workhandler;

import javax.ejb.Local;

/**
 * 用于事务控制
 * @author zoe
 */
@Local
public interface TransactionSessionBeanLocal {

    public void execute(WorkHandler workHandler);
}
