package com.ehr.framework.httpsession;

/**
 * 保存登录人员的信息
 * @author zoe
 */
@Deprecated
public final class HttpSessionEntity {

    HttpSessionEntity() {
    }
    // 公司ID
    private long companyId;
    // 登录帐号ID
    private long userId;
    // 登录人ID
    private long empId;
    // 登录IP
    private String ipAddress;
    //登录loginCode
    
    private String loginCode;

    public String getLoginCode() {
        return loginCode;
    }

    void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }

    void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getCompanyId() {
        return companyId;
    }

    void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getUserId() {
        return userId;
    }

    void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEmpId() {
        return empId;
    }

    void setEmpId(long empId) {
        this.empId = empId;
    }
}
