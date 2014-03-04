package com.ehr.framework.httpsession;

import com.ehr.framework.context.HttpSessionManager;
import com.ehr.framework.worker.HttpServletRequestManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session管理对象
 *
 * @author zoe
 */
@Deprecated
public final class HttpSessionManagerImpl implements HttpSessionManager {
    
    private final HttpServletRequestManager httpServletRequestManager;

    public HttpSessionManagerImpl(final HttpServletRequestManager httpServletRequestManager) {
        this.httpServletRequestManager = httpServletRequestManager;
    }

    /**
     * 保存登录人员信息
     *
     * @param request
     * @param httpSessionEntity
     */
    @Override
    public void setHttpSession(final HttpServletRequest request, final long companyId, final long userId, final long empId) {
        HttpSessionEntity httpSessionEntity = new HttpSessionEntity();
        httpSessionEntity.setCompanyId(companyId);
        httpSessionEntity.setUserId(userId);
        httpSessionEntity.setEmpId(empId);
        String ipAddress = request.getRemoteAddr();
        httpSessionEntity.setIpAddress(ipAddress);
        HttpSession session = request.getSession(true);
        session.setAttribute("httpSessionEntity", httpSessionEntity);
    }

    /**
     * 从session获取登录人员信息
     *
     * @param request
     * @param httpSessionEntity
     * @return
     */
    @Override
    public HttpSessionEntity getHttpSession(final HttpServletRequest request) {
        HttpSessionEntity httpSessionEntity = null;
        if (request != null) {
            HttpSession session = request.getSession();
            Object object = session.getAttribute("httpSessionEntity");
            if (object != null) {
                httpSessionEntity = (HttpSessionEntity) object;
            }
        }
        return httpSessionEntity;
    }

    @Override
    public HttpSessionEntity getThreadLocal() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        return this.getHttpSession(request);
    }
}
