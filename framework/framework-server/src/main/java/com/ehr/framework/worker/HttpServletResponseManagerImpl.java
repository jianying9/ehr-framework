package com.ehr.framework.worker;

import javax.servlet.http.HttpServletResponse;

/**
 * 响应对象的工具类
 * @author zoe
 */
public final class HttpServletResponseManagerImpl implements HttpServletResponseManager{

    private final ThreadLocal<HttpServletResponse> threadLocal = new ThreadLocal<HttpServletResponse>();

    @Override
    public void openThreadLocal(HttpServletResponse response) {
        this.threadLocal.set(response);
    }

    @Override
    public void closeThreadLocal() {
        this.threadLocal.remove();
    }

    @Override
    public HttpServletResponse getThreadLocal() {
        return threadLocal.get();
    }
}
