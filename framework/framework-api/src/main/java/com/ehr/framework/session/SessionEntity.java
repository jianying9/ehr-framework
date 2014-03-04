package com.ehr.framework.session;

/**
 * 保存登录人员的信息
 *
 * @author zoe
 */
public interface SessionEntity {

    public String getLoginCode();

    public long getCompanyId();

    public long getUserId();
}
