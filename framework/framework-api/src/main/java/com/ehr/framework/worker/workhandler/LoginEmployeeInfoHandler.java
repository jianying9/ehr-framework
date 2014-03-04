package com.ehr.framework.worker.workhandler;

/**
 *
 * @author zoe
 */
public interface LoginEmployeeInfoHandler {

    public long inquireEmpIdByCompanyIdAndUserId(long companyId, long userId);
}
