package com.ehr.framework.context;

import com.ehr.framework.httpsession.HttpSessionEntity;
import javax.servlet.http.HttpServletRequest;

/**
 * session管理对象
 * @author zoe
 */
@Deprecated
public interface HttpSessionManager {

    public HttpSessionEntity getHttpSession(final HttpServletRequest request);

    /**
     * 保存登录人员信息
     * @param request
     * @param httpSessionEntity
     */
    public void setHttpSession(final HttpServletRequest request, final long companyId, final long userId, final long empId);

    /**
     * 从当前线程中获取登录信息
     * @return 
     */
    public HttpSessionEntity getThreadLocal();
}
