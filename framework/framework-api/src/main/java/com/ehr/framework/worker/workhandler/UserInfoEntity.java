package com.ehr.framework.worker.workhandler;

import com.ehr.framework.session.SessionEntity;

/**
 * 保存登录人员信息
 *
 * @author zoe
 */
public final class UserInfoEntity {

    UserInfoEntity(SessionEntity sessionEntity, long empId, String ipAddress) {
        this.sessionEntity = sessionEntity;
        this.empId = empId;
        this.ipAddress = ipAddress;
    }
    private SessionEntity sessionEntity;
    // 登录人ID
    private long empId;
    // 登录IP
    private String ipAddress;

    public String getLoginCode() {
        return this.sessionEntity.getLoginCode();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public long getCompanyId() {
        return this.sessionEntity.getCompanyId();
    }

    public long getUserId() {
        return this.sessionEntity.getUserId();
    }

    public long getEmpId() {
        return empId;
    }
}
