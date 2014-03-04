package com.ehr.framework.worker.workhandler;

import com.ehr.framework.cache.ParameterCache;
import com.ehr.framework.worker.HttpServletRequestManager;
import javax.servlet.http.HttpServletRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * 缓存分页参数验证处理类处理类
 *
 * @author zoe
 */
public class CachePageParameterWorkHandlerImpl implements WorkHandler {

    private final WorkHandler nextWorkHandler;
    private final WorkHandler pageWorkHandler;
    private final WritePageParameterManager pageParameterManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final Cache parameterCache;

    public CachePageParameterWorkHandlerImpl(final WorkHandler pageWorkHandler, final Cache parameterCache, final WritePageParameterManager pageParameterManager, final HttpServletRequestManager httpServletRequestManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.pageParameterManager = pageParameterManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.parameterCache = parameterCache;
        this.pageWorkHandler = pageWorkHandler;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String cacheId = request.getParameter("cacheId");
        if (cacheId == null || cacheId.isEmpty()) {
            //没有缓存
            this.pageWorkHandler.execute();
        } else {
            //获取缓存信息
            Element element = this.parameterCache.getQuiet(cacheId);
            if (element == null) {
                //没有缓存
                this.pageWorkHandler.execute();
            } else {
                //有缓存
                ParameterCache paraCache = (ParameterCache) element.getObjectValue();
                String pageIndex = paraCache.getParameter("pageIndex");
                String pageRows = paraCache.getParameter("pageRows");
                PageExtendedEntity pageExtendedEntity = new PageExtendedEntityImpl();
                pageExtendedEntity.setPageIndex(pageIndex);
                pageExtendedEntity.setPageRows(pageRows);
                this.pageParameterManager.openThreadLocal(pageExtendedEntity);
                this.nextWorkHandler.execute();
                this.pageParameterManager.closeThreadLocal();
            }
        }
    }
}
