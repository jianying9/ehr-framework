package com.ehr.framework.worker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务工作对象接口
 * @author zoe
 */
public interface ServiceWorker {

    public void doWork(HttpServletRequest request, HttpServletResponse response);
}
