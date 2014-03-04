package com.ehr.framework.worker;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求对象的工具类
 * @author zoe
 */
public interface HttpServletRequestManager {

    public void openThreadLocal(HttpServletRequest request);

    public void closeThreadLocal();

    public HttpServletRequest getThreadLocal();
}
