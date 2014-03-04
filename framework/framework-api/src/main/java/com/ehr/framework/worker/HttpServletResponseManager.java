package com.ehr.framework.worker;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应对象的工具类
 * @author zoe
 */
public interface HttpServletResponseManager {

    public void openThreadLocal(HttpServletResponse response);

    public void closeThreadLocal();

    public HttpServletResponse getThreadLocal();
}
