package com.ehr.framework.worker.workhandler;

import javax.ejb.*;

/**
 * 用于事务控制
 *
 * @author zoe
 */
@Stateless
@Startup
@EJB(name = "framework/TransactionSessionBean", beanInterface = TransactionSessionBeanLocal.class)
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void execute(final WorkHandler workHandler) {
        workHandler.execute();
    }
}
