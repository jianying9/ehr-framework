package com.ehr.framework.service;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.dictionary.DictionaryContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import com.ehr.framework.entity.EntityHandler;
import com.ehr.framework.entity.ExtendedEntityContextBuilder;
import com.ehr.framework.entity.ExtendedEntityHandler;
import com.ehr.framework.entity.field.FieldContextBuilder;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.privilege.ActionEntity;
import com.ehr.framework.privilege.VersionType;
import com.ehr.framework.worker.ServiceWorker;
import com.ehr.framework.worker.ServiceWorkerContextBuilder;
import com.ehr.framework.worker.ServiceWorkerImpl;
import com.ehr.framework.worker.workhandler.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;

/**
 * 负责解析annotation ServiceConfig
 *
 * @author zoe
 */
public class ServiceConfigParser<K extends Service, T extends Entity> {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param clazz
     * @param serviceCtxBuilder
     */
    public void parse(final Class<K> clazz, final ServiceWorkerContextBuilder serviceCtxBuilder) {
        this.logger.debug("-----------------parsing service {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(ServiceConfig.class)) {
            //1.获取注解ServiceConfig
            final ServiceConfig serviceConfig = clazz.getAnnotation(ServiceConfig.class);
            final String actionName = serviceConfig.actionName();
            final String[] importantParameter = serviceConfig.importantParameter();
            final String[] minorParameter = serviceConfig.minorParameter();
            final MinorHandlerTypeEnum minorHandlerTypeEnum = serviceConfig.minorHandlerTypeEnum();
            final String[] returnParameter = serviceConfig.returnParameter();
            final String[] entityNames = serviceConfig.entityNames();
            final String[] extendedEntityNames = serviceConfig.extendedEntityNames();
            final ParameterTypeEnum parameterTypeEnum = serviceConfig.parameterTypeEnum();
            final ResponseTypeEnum responseTypeEnum = serviceConfig.responseTypeEnum();
            final boolean requireTransaction = serviceConfig.requireTransaction();
            final String exportFileName = serviceConfig.exportFileName();
            final String[] cacheFlag = serviceConfig.cacheFlag();
            //获取字段处理对象集合
            final Map<String, FieldHandler> fieldHandlerMapTemp = new HashMap<String, FieldHandler>(2, 1);
            EntityHandler<T> entityHandler;
            Set<Entry<String, FieldHandler>> entrySet;
            Map<String, FieldHandler> fieldHandlerMapTmp;
            final EntityContextBuilder<T> entityContextBuilder = serviceCtxBuilder.getEntityContextBuilder();
            final FieldContextBuilder fieldContextBuilder = serviceCtxBuilder.getFieldContextBuilder();
            //实体参数
            for (String entityName : entityNames) {
                entityHandler = entityContextBuilder.getEntityHandler(entityName);
                //如果entity找不到，则抛出异常，停止加载
                if (entityHandler == null) {
                    StringBuilder mesBuilder = new StringBuilder(512);
                    mesBuilder.append("There was an error parsing service worker. Cause: can not find entity handler : ").append(entityName);
                    mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                    throw new RuntimeException(mesBuilder.toString());
                }
                fieldHandlerMapTmp = entityHandler.getFieldHandlerMap();
                entrySet = entityHandler.getFieldHandlerMap().entrySet();
                for (Entry<String, FieldHandler> entry : entrySet) {
                    if (!fieldHandlerMapTemp.containsKey(entry.getKey())) {
                        fieldHandlerMapTemp.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            //扩展参数
            final ExtendedEntityContextBuilder extendedEntityContextBuilder = serviceCtxBuilder.getExtendedEntityContextBuilder();
            ExtendedEntityHandler extendedEntityHandler;
            for (String extendedEntityName : extendedEntityNames) {
                extendedEntityHandler = extendedEntityContextBuilder.getExtendedEntityHandler(extendedEntityName);
                //如果extended entity找不到，则抛出异常，停止加载
                if (extendedEntityHandler == null) {
                    StringBuilder mesBuilder = new StringBuilder(512);
                    mesBuilder.append("There was an error parsing service worker. Cause: can not find extended entity handler : ").append(extendedEntityName);
                    mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                    throw new RuntimeException(mesBuilder.toString());
                }
                fieldHandlerMapTmp = extendedEntityHandler.getFieldHandlerMap();
                entrySet = extendedEntityHandler.getFieldHandlerMap().entrySet();
                for (Entry<String, FieldHandler> entry : entrySet) {
                    if (!fieldHandlerMapTemp.containsKey(entry.getKey())) {
                        fieldHandlerMapTemp.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            final Map<String, FieldHandler> fieldHandlerMap = new HashMap<String, FieldHandler>(2, 1);
            //获取必要参数处理对象
            FieldHandler fieldHandler;
            for (String parameter : importantParameter) {
                fieldHandler = fieldHandlerMapTemp.get(parameter);
                if (fieldHandler == null) {
                    StringBuilder mesBuilder = new StringBuilder(512);
                    mesBuilder.append("There was an error parsing service worker. Cause: can not find important parameter config : ").append(parameter);
                    mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                    throw new RuntimeException(mesBuilder.toString());
                }
                if (!fieldHandlerMap.containsKey(parameter)) {
                    fieldHandlerMap.put(parameter, fieldHandler);
                }
            }
            //如果输入数据是EXCEL则跳过次要参数验证
            if (parameterTypeEnum != ParameterTypeEnum.EXCEL_DATA) {
                //获取次要参数
                for (String parameter : minorParameter) {
                    fieldHandler = fieldHandlerMapTemp.get(parameter);
                    if (fieldHandler == null) {
                        StringBuilder mesBuilder = new StringBuilder(512);
                        mesBuilder.append("There was an error parsing service worker. Cause: can not find minor parameter config : ").append(parameter);
                        mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                        throw new RuntimeException(mesBuilder.toString());
                    }
                    if (!fieldHandlerMap.containsKey(parameter)) {
                        fieldHandlerMap.put(parameter, fieldHandler);
                    }
                }
            }
            //获取返回参数
            for (String parameter : returnParameter) {
                fieldHandler = fieldHandlerMapTemp.get(parameter);
                if (fieldHandler == null) {
                    StringBuilder mesBuilder = new StringBuilder(512);
                    mesBuilder.append("There was an error parsing service worker. Cause: can not find return parameter config : ").append(parameter);
                    mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                    throw new RuntimeException(mesBuilder.toString());
                }
                if (!fieldHandlerMap.containsKey(parameter)) {
                    fieldHandlerMap.put(parameter, fieldHandler);
                }
            }
            //开始生成业务处理链
            //实例化该clazz
            Service service = null;
            try {
                service = clazz.newInstance();
            } catch (Exception e) {
                this.logger.error("There was an error instancing class {}. Cause: {}", clazz.getName(), e.getMessage());
                throw new RuntimeException("There wa an error instancing class ".concat(clazz.getName()));
            }
            WorkHandler workHandler = service;
            //判断是否需要事务，如果需要则加入事务处理环节
            if (requireTransaction) {
                workHandler = new TransactionWorkHandlerImpl(serviceCtxBuilder.getTransactionSessionBeanLocalFactory(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
            } else {
                workHandler = new NoTransactionWorkHandlerImpl(serviceCtxBuilder.getResponseWriterManager(), workHandler);
            }
            //判断是否有缓存提交参数
            boolean hasParameterCache = false;
            if ((parameterTypeEnum == ParameterTypeEnum.SIMPLE_MAP || parameterTypeEnum == ParameterTypeEnum.BATCH_MAP) && cacheFlag.length > 0) {
                boolean batchParameter = false;
                if (parameterTypeEnum == ParameterTypeEnum.BATCH_MAP) {
                    batchParameter = true;
                }
                boolean page = false;
                if (responseTypeEnum == ResponseTypeEnum.JSON_PAGE) {
                    page = true;
                }
                workHandler = new ParameterCacheWriteWorkHandlerImpl(batchParameter, cacheFlag, page, serviceCtxBuilder.getParameterCache(), serviceCtxBuilder.getPageParameterManager(), serviceCtxBuilder.getResponseWriterManager(), serviceCtxBuilder.getSimpleMapManager(), serviceCtxBuilder.getBatchMapManager(), workHandler);
                hasParameterCache = true;
            }
            //判断是否存在动态数据字段
            final DictionaryContextBuilder<T> dictionaryContextBuilder = serviceCtxBuilder.getDictionaryContextBuilder();
            final Set<String> dynamicFieldSet = new HashSet<String>(2, 1);
            String dynamicDictionaryName;
            for (String parameter : returnParameter) {
                fieldHandler = fieldHandlerMap.get(parameter);
                dynamicDictionaryName = fieldHandler.dynamicDictionaryName();
                if (!dynamicDictionaryName.isEmpty()) {
                    //验证动态数据字典处理类是否存在
                    if (!dictionaryContextBuilder.assertExistDynamicDictionary(dynamicDictionaryName)) {
                        StringBuilder mesBuilder = new StringBuilder(512);
                        mesBuilder.append("There was an error parsing service worker. Cause: can not find dynamicDictionaryHandler : ").append(dynamicDictionaryName);
                        mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                        mesBuilder.append("\n").append("error parameter is ").append(parameter);
                        throw new RuntimeException(mesBuilder.toString());
                    }
                    dynamicFieldSet.add(parameter);
                }
            }
            //获取伪实现配制,如果当前服务配置为伪实现或则服务所属模块配置为伪实现，则该服务采用伪实现
            boolean pseudo = false;
            int versionType = VersionType.NOT_ACTIVATE;
            Map<String, ActionEntity> actionMap = serviceCtxBuilder.getActionMap();
            ActionEntity actionEntity = actionMap.get(actionName);
            if (actionEntity != null) {
                versionType = actionEntity.getVersionType();
                if (serviceCtxBuilder.isUsePseudo() && actionEntity.isPseudo()) {
                    pseudo = true;
                }
            }
            if (pseudo) {
                switch (responseTypeEnum) {
                    case JSON:
                        workHandler = new PseudoJsonWorkHandlerImpl(returnParameter, fieldHandlerMap, serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                        break;
                    case JSON_PAGE:
                        workHandler = new PseudoJsonPageWorkHandlerImpl(serviceCtxBuilder.getPageParameterManager(), returnParameter, fieldHandlerMap, serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                        break;
                    case EXCEL:
                        workHandler = new PseudoExcelWorkHandlerImpl(returnParameter, fieldHandlerMap, exportFileName, serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                        break;
                }
            }
            if (dynamicFieldSet.isEmpty()) {
                //判断响应类型,将对应的响应对象加入到处理环节
                workHandler = this.createResponseWorkHandler(responseTypeEnum, returnParameter, fieldHandlerMap, exportFileName, workHandler, serviceCtxBuilder);
            } else {
                WorkHandler responseWorkHandler = this.createResponseWorkHandler(responseTypeEnum, returnParameter, fieldHandlerMap, exportFileName, null, serviceCtxBuilder);
                workHandler = new DynamicDictionaryWorkHandlerImpl(responseWorkHandler, returnParameter, dynamicFieldSet, fieldContextBuilder.getWriteDynamicDictionaryManager(), fieldHandlerMap, serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
            }
            //判断取是否分页输出，如果需要则加入分页参数处理环节
            if (responseTypeEnum == ResponseTypeEnum.JSON_PAGE) {
                ExtendedEntityHandler pageExtendedEntityHandler = extendedEntityContextBuilder.getExtendedEntityHandler("PageExtended");
                Map<String, FieldHandler> pageFieldHandlerMap = pageExtendedEntityHandler.getFieldHandlerMap();
                WorkHandler pageWorkHandler = new PageParameterWorkHandlerImpl(pageFieldHandlerMap, serviceCtxBuilder.getPageParameterManager(), serviceCtxBuilder.getHttpServletRequestManager(), workHandler);
                if (hasParameterCache) {
                    workHandler = new CachePageParameterWorkHandlerImpl(pageWorkHandler, serviceCtxBuilder.getParameterCache(), serviceCtxBuilder.getPageParameterManager(), serviceCtxBuilder.getHttpServletRequestManager(), workHandler);
                } else {
                    workHandler = pageWorkHandler;
                }
            }
            //判断取值验证类型,将对应处理对象加入到处理环节
            String firstParameter;
            FieldHandler firstFieldHandler;
            switch (parameterTypeEnum) {
                case NO_PARAMETER:
                    workHandler = new NoParaWorkHandlerImpl(serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                    break;
                case SIMPLE_PARAMETER:
                    if (importantParameter.length == 0) {
                        StringBuilder mesBuilder = new StringBuilder(512);
                        mesBuilder.append("There was an error parsing service worker. Cause: get parameter type is SIMPLE_PARAMETER , but important parameter length = 0.");
                        mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                        throw new RuntimeException(mesBuilder.toString());
                    }
                    firstParameter = importantParameter[0];
                    firstFieldHandler = fieldHandlerMap.get(firstParameter);
                    workHandler = new SimpleParaWorkHandlerImpl(firstParameter, firstFieldHandler, serviceCtxBuilder.getSimpleParaManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                    break;
                case BATCH_PARAMETER:
                    if (importantParameter.length == 0) {
                        StringBuilder mesBuilder = new StringBuilder(512);
                        mesBuilder.append("There was an error parsing service worker. Cause: get parameter type is BATCH_PARAMETER , but important parameter length = 0.");
                        mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                        throw new RuntimeException(mesBuilder.toString());
                    }
                    firstParameter = importantParameter[0];
                    firstFieldHandler = fieldHandlerMap.get(firstParameter);
                    workHandler = new BatchParaWorkHandlerImpl(firstParameter, firstFieldHandler, serviceCtxBuilder.getBatchParaManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                    break;
                case SIMPLE_MAP:
                    WorkHandler simpleMapWorkHandler = null;
                    switch (minorHandlerTypeEnum) {
                        case KEEP_EMPTY:
                            simpleMapWorkHandler = new SimpleMapKeepEmptyWorkHandlerImpl(importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getSimpleMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                            break;
                        case DISCARD_EMPTY:
                            simpleMapWorkHandler = new SimpleMapDiscardEmptyWorkHandlerImpl(importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getSimpleMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                            break;
                        case DEFAULT_REPLACE_NULL_AND_EMPTY:
                            simpleMapWorkHandler = new SimpleMapDefaultReplaceNullAndEmptyWorkHandlerImpl(importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getSimpleMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                            break;
                    }
                    if (hasParameterCache) {
                        workHandler = new CacheSimpleMapKeepEmptyWorkHandlerImpl(serviceCtxBuilder.getParameterCache(), simpleMapWorkHandler, importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getSimpleMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                    } else {
                        workHandler = simpleMapWorkHandler;
                    }
                    break;
                case BATCH_MAP:
                    WorkHandler batchMapWorkHandler = new BatchMapWorkHandlerImpl(importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getBatchMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler, serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getAccessActionLogger());
                    if (hasParameterCache) {
                        workHandler = new CacheBatchMapWorkHandlerImpl(serviceCtxBuilder.getParameterCache(), batchMapWorkHandler, importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getBatchMapManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                    } else {
                        workHandler = batchMapWorkHandler;
                    }
                    break;
                case FILE_ITEM_LIST:
                    workHandler = new FileitemWorkHandlerImpl(serviceCtxBuilder.getFileItemManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
                    break;
                case EXCEL_DATA:
                    workHandler = new ExcelDataWorkHandlerImpl(serviceCtxBuilder.getUploadFileManager(), importantParameter, minorParameter, fieldHandlerMap, serviceCtxBuilder.getExcelDataManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
            }
            //判断是否登录验证
            if (versionType >= VersionType.NOT_ACTIVATE) {
                workHandler = new PrivilegeWorkHandlerImpl(serviceCtxBuilder.getLoginEmployeeInfoHandler(), serviceCtxBuilder.getSessionEntityHandler(), serviceCtxBuilder.getCustomPrivilegeHandler(), serviceCtxBuilder.getVersionPrivilegeHandler(), serviceCtxBuilder.getWriteSessionManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager(), serviceCtxBuilder.getResponseWriterManager(), workHandler);
            }
            //创建对应的工作对象
            final ServiceWorker serviceWorker = new ServiceWorkerImpl(actionName, workHandler, serviceCtxBuilder.getResponseWriterManager(), serviceCtxBuilder.getHttpServletRequestManager(), serviceCtxBuilder.getHttpServletResponseManager());
            serviceCtxBuilder.putServiceWorker(actionName, serviceWorker, clazz.getName());
            this.logger.debug("-----------------parse service {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse service {} missing annotation ServiceConfig-----------------", clazz.getName());
        }
    }

    private WorkHandler createResponseWorkHandler(final ResponseTypeEnum responseTypeEnum, final String[] returnParameter, final Map<String, FieldHandler> fieldHandlerMap, final String exportFileName, final WorkHandler workHandler, final ServiceWorkerContextBuilder appCtxBuilder) {
        WorkHandler responseWorkHandler = null;
        switch (responseTypeEnum) {
            case JSON:
                responseWorkHandler = new JsonWriterWorkHandlerImpl(returnParameter, fieldHandlerMap, appCtxBuilder.getHttpServletRequestManager(), appCtxBuilder.getHttpServletResponseManager(), appCtxBuilder.getResponseWriterManager(), workHandler);
                break;
            case JSON_PAGE:
                responseWorkHandler = new JsonPageWriterWorkHandlerImpl(returnParameter, fieldHandlerMap, appCtxBuilder.getHttpServletRequestManager(), appCtxBuilder.getHttpServletResponseManager(), appCtxBuilder.getResponseWriterManager(), workHandler);
                break;
            case EXCEL:
                responseWorkHandler = new ExcelWriterWorkHandlerImpl(returnParameter, fieldHandlerMap, exportFileName, appCtxBuilder.getHttpServletRequestManager(), appCtxBuilder.getHttpServletResponseManager(), appCtxBuilder.getResponseWriterManager(), workHandler);
                break;
            case EXCEL_MODEL:
                responseWorkHandler = new ExcelModelWriterWorkHandlerImpl(returnParameter, fieldHandlerMap, exportFileName, appCtxBuilder.getHttpServletRequestManager(), appCtxBuilder.getHttpServletResponseManager(), appCtxBuilder.getResponseWriterManager(), workHandler);
                break;
            case NO:
                responseWorkHandler = workHandler;
                break;
        }
        return responseWorkHandler;
    }
}
