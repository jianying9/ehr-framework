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
 * 多参数多值取值、验证处理类处理类
 *
 * @author zoe
 */
public class CacheBatchMapWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final WorkHandler batchMapWorkHandler;
    private final String[] importantParameter;
    private final String[] minorParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final WriteBatchMapManager BatchMapManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;
    private final Cache parameterCache;

    public CacheBatchMapWorkHandlerImpl(final Cache parameterCache, final WorkHandler batchMapWorkHandler, final String[] importantParameter, final String[] minorParameter, final Map<String, FieldHandler> fieldHandlerMap, final WriteBatchMapManager BatchMapManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.batchMapWorkHandler = batchMapWorkHandler;
        this.importantParameter = importantParameter;
        this.minorParameter = minorParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.BatchMapManager = BatchMapManager;
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
            this.batchMapWorkHandler.execute();
        } else {
            //获取缓存信息
            ResponseFlagType flag = DefaultResponseFlagEnum.SUCCESS;
            Element element = this.parameterCache.getQuiet(cacheId);
            Map<String, String[]> parameterMap = new HashMap<String, String[]>(importantParameter.length + minorParameter.length, 1);
            String[] parameterValues;
            String errorMsg = "";
            String paraValue = "";
            String errorParaName = "";
            FieldHandler fieldHandler;
            if (element == null) {
                //超时
                flag = DefaultResponseFlagEnum.TIMEOUT;
            } else {
                //有缓存
                ParameterCache paraCache = (ParameterCache) element.getObjectValue();
                for (String parameter : this.importantParameter) {
                    parameterValues = paraCache.getParameterValues(parameter);
                    if (parameterValues == null) {
                        flag = DefaultResponseFlagEnum.TIMEOUT;
                        break;
                    } else {
                        parameterMap.put(parameter, parameterValues);
                    }
                }
                for (String parameter : this.minorParameter) {
                    parameterValues = paraCache.getParameterValues(parameter);
                    if (parameterValues != null) {
                        parameterMap.put(parameter, parameterValues);
                    }
                }
            }
            if (flag == DefaultResponseFlagEnum.SUCCESS) {
                for (String parameter : this.minorParameter) {
                    parameterValues = request.getParameterValues(parameter);
                    if (parameterValues != null) {
                        fieldHandler = this.fieldHandlerMap.get(parameter);
                        for (int index = 0; index < parameterValues.length; index++) {
                            paraValue = parameterValues[index];
                            paraValue = HttpUtils.trim(paraValue);
                            if (paraValue.isEmpty()) {
                                paraValue = fieldHandler.getDefaultValue();
                                parameterValues[index] = paraValue;
                            } else {
                                errorMsg = fieldHandler.validate(paraValue);
                                if (errorMsg.isEmpty()) {
                                    parameterValues[index] = paraValue;
                                } else {
                                    errorParaName = parameter;
                                    break;
                                }
                            }
                        }
                        if (errorMsg.isEmpty()) {
                            parameterMap.put(parameter, parameterValues);
                        } else {
                            break;
                        }
                    }
                }
            }
            if (errorMsg.isEmpty() && flag == DefaultResponseFlagEnum.SUCCESS) {
                this.BatchMapManager.openThreadLocal(parameterMap);
                this.nextWorkHandler.execute();
                this.BatchMapManager.closeThreadLocal();
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
