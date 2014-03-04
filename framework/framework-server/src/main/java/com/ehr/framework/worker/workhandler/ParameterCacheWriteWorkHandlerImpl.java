package com.ehr.framework.worker.workhandler;

import com.ehr.framework.cache.ParameterCache;
import com.ehr.framework.context.BatchMapManager;
import com.ehr.framework.context.PageParameterManager;
import com.ehr.framework.context.SimpleMapManager;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.Map;
import java.util.UUID;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;

/**
 * 保存当前请求
 *
 * @author zoe
 */
public class ParameterCacheWriteWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final boolean batchParameter;
    private final String[] cacheFlag;
    private final Cache parameterCache;
    private final ResponseWriterManager responseWriterManager;
    private final SimpleMapManager simpleMapManager;
    private final BatchMapManager batchMapManager;
    private final boolean page;
    private final PageParameterManager pageParameterManager;

    public ParameterCacheWriteWorkHandlerImpl(
            final boolean batchParameter,
            final String[] cacheFlag,
            final boolean page,
            final Cache parameterCache,
            final PageParameterManager pageParameterManager,
            final ResponseWriterManager responseWriterManager,
            final SimpleMapManager simpleMapManager,
            final BatchMapManager batchMapManager,
            final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.cacheFlag = cacheFlag;
        this.responseWriterManager = responseWriterManager;
        this.parameterCache = parameterCache;
        this.batchParameter = batchParameter;
        this.simpleMapManager = simpleMapManager;
        this.batchMapManager = batchMapManager;
        this.page = page;
        this.pageParameterManager = pageParameterManager;
    }

    @Override
    public void execute() {
        this.nextWorkHandler.execute();
        ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
        String flag = responseWriter.getFlag();
        boolean result = false;
        for (String flg : this.cacheFlag) {
            if (flg.equals(flag)) {
                result = true;
                break;
            }
        }
        if (result) {
            //需要缓存
            ParameterCache paraCache;
            if (this.batchParameter) {
                Map<String, String[]> batchMap = this.batchMapManager.getThreadLocal();
                paraCache = new ParameterCache(batchMap.size());
                paraCache.setParameterMaps(batchMap);
            } else {
                Map<String, String> simpleMap = this.simpleMapManager.getThreadLocal();
                paraCache = new ParameterCache(simpleMap.size());
                paraCache.setParameterMap(simpleMap);
            }
            if (this.page) {
                PageExtendedEntity pageExtendedEntity = this.pageParameterManager.getThreadLocal();
                paraCache.setParameter("pageIndex", Integer.toString(pageExtendedEntity.getPageIndex()));
                paraCache.setParameter("pageRows", Integer.toString(pageExtendedEntity.getPageRows()));
            }
            String uuid = UUID.randomUUID().toString();
            Element element = new Element(uuid, paraCache);
            this.parameterCache.putQuiet(element);
            responseWriter.setCacheId(uuid);
        }
    }
}
