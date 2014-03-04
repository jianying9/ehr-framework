package com.ehr.framework.worker.workhandler;

import com.ehr.framework.cache.ParameterCache;
import com.ehr.framework.config.DefaultResponseFlagEnum;
import com.ehr.framework.config.ResponseFlagType;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;

/**
 * 多个参数单值处理类,保持次要参数空字符
 *
 * @author zoe
 */
public class CacheSimpleMapKeepEmptyWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final WorkHandler simpleMapWorkHandler;
    private final String[] importantParameter;
    private final String[] minorParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final WriteSimpleMapManager simpleMapManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final Cache parameterCache;

    public CacheSimpleMapKeepEmptyWorkHandlerImpl(final Cache parameterCache, final WorkHandler simpleMapWorkHandler, final String[] importantParameter, final String[] minorParameter, final Map<String, FieldHandler> fieldHandlerMap, final WriteSimpleMapManager simpleMapManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.simpleMapWorkHandler = simpleMapWorkHandler;
        this.importantParameter = importantParameter;
        this.minorParameter = minorParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.simpleMapManager = simpleMapManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
        this.parameterCache = parameterCache;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String cacheId = request.getParameter("cacheId");
        if (cacheId == null || cacheId.isEmpty()) {
            //没有缓存
            this.simpleMapWorkHandler.execute();
        } else {
            //获取缓存信息
            ResponseFlagType flag = DefaultResponseFlagEnum.SUCCESS;
            Element element = this.parameterCache.getQuiet(cacheId);
            String paraValue = "";
            String errorParaName = "";
            String errorMsg = "";
            FieldHandler fieldHandler;
            Map<String, String> parameterMap = new HashMap<String, String>(importantParameter.length + minorParameter.length, 1);
            if (element == null) {
                //超时
                flag = DefaultResponseFlagEnum.TIMEOUT;
            } else {
                //有缓存
                ParameterCache paraCache = (ParameterCache) element.getObjectValue();
                for (String parameter : this.importantParameter) {
                    paraValue = paraCache.getParameter(parameter);
                    if (paraValue == null) {
                        flag = DefaultResponseFlagEnum.TIMEOUT;
                        break;
                    } else {
                        parameterMap.put(parameter, paraValue);
                    }
                }
                for (String parameter : this.minorParameter) {
                    paraValue = paraCache.getParameter(parameter);
                    if (paraValue != null) {
                        parameterMap.put(parameter, paraValue);
                    }
                }
                if (flag == DefaultResponseFlagEnum.SUCCESS) {
                    for (String parameter : this.minorParameter) {
                        paraValue = request.getParameter(parameter);
                        if (paraValue != null) {
                            paraValue = HttpUtils.trim(paraValue);
                            fieldHandler = this.fieldHandlerMap.get(parameter);
                            errorMsg = fieldHandler.validate(paraValue);
                            if (errorMsg.isEmpty()) {
                                parameterMap.put(parameter, paraValue);
                            } else {
                                errorParaName = parameter;
                                break;
                            }
                        }
                    }
                }
            }
            if (errorMsg.isEmpty() && flag == DefaultResponseFlagEnum.SUCCESS) {
                this.simpleMapManager.openThreadLocal(parameterMap);
                this.nextWorkHandler.execute();
                this.simpleMapManager.closeThreadLocal();
            } else {
                ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
                HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
                if (flag == DefaultResponseFlagEnum.TIMEOUT) {
                    responseWriter.timeOut();
                } else {
                    errorMsg = errorParaName.concat(errorMsg);
                    this.logger.debug("get parameter failure. Cause: {},value:{}", errorMsg, paraValue);
                    responseWriter.invalid();
                    responseWriter.setInfo(errorMsg);
                }
                responseWriter.writeJson(request, response);
            }
        }
    }
}
