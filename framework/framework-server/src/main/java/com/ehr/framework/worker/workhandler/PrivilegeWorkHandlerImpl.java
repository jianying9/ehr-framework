package com.ehr.framework.worker.workhandler;

import com.ehr.framework.privilege.CustomPrivilegeHandler;
import com.ehr.framework.privilege.VersionPrivilegeHandler;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.session.SessionEntity;
import com.ehr.framework.session.SessionEntityHandler;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录权限处理类
 *
 * @author zoe
 */
public class PrivilegeWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final WriteSessionManager writeSessionManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final SessionEntityHandler sessionEntityHandler;
    private final VersionPrivilegeHandler versionPrivilegeHandler;
    private final CustomPrivilegeHandler customPrivilegeHandler;
    private final LoginEmployeeInfoHandler loginEmployeeInfoHandler;

    public PrivilegeWorkHandlerImpl(final LoginEmployeeInfoHandler loginEmployeeInfoHandler, final SessionEntityHandler sessionEntityHandler, final CustomPrivilegeHandler customPrivilegeHandler, final VersionPrivilegeHandler versionPrivilegeHandler, final WriteSessionManager writeSessionManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.writeSessionManager = writeSessionManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.sessionEntityHandler = sessionEntityHandler;
        this.versionPrivilegeHandler = versionPrivilegeHandler;
        this.customPrivilegeHandler = customPrivilegeHandler;
        this.loginEmployeeInfoHandler = loginEmployeeInfoHandler;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String loginCode = request.getParameter("loginCode");
        String entry = request.getParameter("entry");
        String seed = request.getParameter("seed");
        SessionEntity sessionEntity = this.sessionEntityHandler.inquireSessionByLoginCode(loginCode, entry, seed);
        if (sessionEntity == null) {
            //未登录，返回提示
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            responseWriter.unLogin();
            HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
            responseWriter.writeJson(request, response);
        } else {
            String actionName = request.getParameter("act");
            long companyId = sessionEntity.getCompanyId();
            boolean privilegeFlag = this.versionPrivilegeHandler.canAccessAction(companyId, actionName);
            if (privilegeFlag) {
                long userId = sessionEntity.getUserId();
                privilegeFlag = this.customPrivilegeHandler.canAccessAction(companyId, userId, actionName);
                if (privilegeFlag) {
                    long loginEmpId = -1;
                    //可以访问
                    if (userId == -1) {
                        //无帐号登录，从请求获取登录信息
                        String loginEmpIdStr = request.getParameter("loginEmpId");
                        if (loginEmpIdStr != null) {
                            try {
                                loginEmpId = Long.parseLong(loginEmpIdStr);
                            } catch (NumberFormatException e) {
                                loginEmpId = -1;
                            }
                        }
                    } else {
                        //从数据库获取登录人员ID
                        loginEmpId = this.loginEmployeeInfoHandler.inquireEmpIdByCompanyIdAndUserId(companyId, userId);
                    }
                    if (loginEmpId == -1) {
                        //无操作人员
                        ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
                        responseWriter.unLogin();
                        HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
                        responseWriter.writeJson(request, response);
                    } else {
                        //构造登录用户信息放入线程
                        String ipAddress = request.getRemoteAddr();
                        UserInfoEntity userInfoEntity = new UserInfoEntity(sessionEntity, loginEmpId, ipAddress);
                        this.writeSessionManager.openThreadLocal(userInfoEntity);
                        this.nextWorkHandler.execute();
                        this.writeSessionManager.closeThreadLocal();
                    }
                } else {
                    //无权限访问
                    ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
                    responseWriter.denied();
                    HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
                    responseWriter.writeJson(request, response);
                }
            } else {
                //无权限访问
                ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
                responseWriter.denied();
                HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
                responseWriter.writeJson(request, response);
            }
        }
    }
}
