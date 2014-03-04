package com.ehr.framework.logger;

/**
 *
 * @author zoe
 */
public class AccessActionLog {
    private long id;
    private long userId;
    private long companyId;
    private String localURL;
    private String referURL;
    private String action;
    private String parameter;
    private String dateTime;
    
    public AccessActionLog() {
        this.userId = -1;
        this.companyId = -1;
        this.localURL = "";
        this.referURL = "";
        this.action = "";
        this.parameter = "";
        this.dateTime = "";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalURL() {
        return localURL;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getReferURL() {
        return referURL;
    }

    public void setReferURL(String referURL) {
        this.referURL = referURL;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
