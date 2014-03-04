package com.ehr.framework.worker;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求对象的工具类
 * @author zoe
 */
public final class HttpServletRequestManagerImpl implements HttpServletRequestManager{

    private final ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

    @Override
    public void openThreadLocal(HttpServletRequest request) {
        this.threadLocal.set(request);
    }

    @Override
    public void closeThreadLocal() {
        this.threadLocal.remove();
    }

    @Override
    public HttpServletRequest getThreadLocal() {
        return threadLocal.get();
    }
}
