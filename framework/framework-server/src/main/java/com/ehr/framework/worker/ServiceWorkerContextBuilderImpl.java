package com.ehr.framework.worker;

import com.ehr.framework.context.TransactionSessionBeanLocalFactory;
import com.ehr.framework.dao.Entity;
import com.ehr.framework.dictionary.DictionaryContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import com.ehr.framework.entity.ExtendedEntityContextBuilder;
import com.ehr.framework.entity.field.FieldContextBuilder;
import com.ehr.framework.file.UploadFileManager;
import com.ehr.framework.logger.AccessActionLogger;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.privilege.ActionEntity;
import com.ehr.framework.privilege.CustomPrivilegeHandler;
import com.ehr.framework.privilege.ModuleEntity;
import com.ehr.framework.privilege.VersionPrivilegeHandler;
import com.ehr.framework.session.SessionEntityHandler;
import com.ehr.framework.worker.workhandler.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.sf.ehcache.Cache;
import org.slf4j.Logger;

/**
 * 全局信息构造类
 *
 * @author zoe
 */
public class ServiceWorkerContextBuilderImpl<T extends Entity> implements ServiceWorkerContextBuilder<T> {

    private final Logger logger = LogFactory.getFrameworkLogger();
    //事务控制类
    private final TransactionSessionBeanLocalFactory transactionSessionBeanLocalFactory;
    private final Map<String, ModuleEntity> moduleMap;
    private final Map<String, ActionEntity> actionMap;
    private final Map<String, String> existClassMap = new HashMap<String, String>(1024);
    private final boolean usePseudo;
    private final Cache parameterCache;
    private final VersionPrivilegeHandler versionPrivilegeHandler;
    private final CustomPrivilegeHandler customPrivilegeHandler;
    private final SessionEntityHandler sessionEntityHandler;
    private final LoginEmployeeInfoHandler loginEmployeeInfoHandler;
    private final AccessActionLogger accessActionLogger;

    @Override
    public Cache getParameterCache() {
        return parameterCache;
    }

    @Override
    public boolean isUsePseudo() {
        return usePseudo;
    }

    @Override
    public final TransactionSessionBeanLocalFactory getTransactionSessionBeanLocalFactory() {
        return this.transactionSessionBeanLocalFactory;
    }
    //参数管理对象
    private final SimpleParaManagerImpl simpleParaManager;

    @Override
    public final SimpleParaManagerImpl getSimpleParaManager() {
        return this.simpleParaManager;
    }
    private final WriteBatchParaManager batchParaManager;

    @Override
    public final WriteBatchParaManager getBatchParaManager() {
        return this.batchParaManager;
    }
    private final SimpleMapManagerImpl simpleMapManager;

    @Override
    public final SimpleMapManagerImpl getSimpleMapManager() {
        return this.simpleMapManager;
    }
    private final WriteFileItemManager fileItemManager;

    @Override
    public final WriteFileItemManager getFileItemManager() {
        return this.fileItemManager;
    }
    private final WriteBatchMapManager batchMapManager;

    @Override
    public final WriteBatchMapManager getBatchMapManager() {
        return this.batchMapManager;
    }
    private final PageParameterManagerImpl pageParameterManager;

    @Override
    public final PageParameterManagerImpl getPageParameterManager() {
        return this.pageParameterManager;
    }
    private final ResponseWriterManager responseWriterManager;

    @Override
    public final ResponseWriterManager getResponseWriterManager() {
        return this.responseWriterManager;
    }
    private final HttpServletRequestManager httpServletRequestManager;

    @Override
    public final HttpServletRequestManager getHttpServletRequestManager() {
        return this.httpServletRequestManager;
    }
    private final HttpServletResponseManager httpServletResponseManager;

    @Override
    public final HttpServletResponseManager getHttpServletResponseManager() {
        return this.httpServletResponseManager;
    }
    private final WriteExcelDataManager excelDataManager;

    @Override
    public final WriteExcelDataManager getExcelDataManager() {
        return this.excelDataManager;
    }
    private final WriteSessionManager writeSessionManager;

    @Override
    public final WriteSessionManager getWriteSessionManager() {
        return this.writeSessionManager;
    }
    //服务集合
    private final Map<String, ServiceWorker> serviceWorkerMap;

    @Override
    public final void putServiceWorker(final String actionName, final ServiceWorker serviceWorker, String className) {
        if (this.serviceWorkerMap.containsKey(actionName)) {
            String existClassName = this.existClassMap.get(actionName);
            if (existClassName == null) {
                existClassName = "NULL";
            }
            StringBuilder errBuilder = new StringBuilder(1024);
            errBuilder.append("There was an error putting service worker. Cause: actionName reduplicated : ").append(actionName).append("\n").append("exist class : ").append(existClassName).append("\n").append("this class : ").append(className);
            throw new RuntimeException(errBuilder.toString());
        }
        this.serviceWorkerMap.put(actionName, serviceWorker);
        this.existClassMap.put(actionName, className);
    }
    private final EntityContextBuilder<T> entityContextBuilder;
    private final ExtendedEntityContextBuilder extendedEntityContextBuilder;
    private final UploadFileManager uploadFileManager;
    private final DictionaryContextBuilder<T> dictionaryContextBuilder;

    /**
     * 构造函数
     *
     * @param properties
     */
    public ServiceWorkerContextBuilderImpl(
            final LoginEmployeeInfoHandler loginEmployeeInfoHandler,
            final SessionEntityHandler sessionEntityHandler,
            final VersionPrivilegeHandler versionPrivilegeHandler,
            final CustomPrivilegeHandler customPrivilegeHandler,
            final boolean usePseudo,
            final EntityContextBuilder<T> entityContextBuilder,
            final ExtendedEntityContextBuilder extendedEntityContextBuilder,
            final UploadFileManager uploadFileManager,
            final HttpServletRequestManager httpServletRequestManager,
            final WriteSessionManager writeSessionManager,
            final TransactionSessionBeanLocalFactory transactionSessionBeanLocalFactory,
            final DictionaryContextBuilder<T> dictionaryContextBuilder, Map<String, ModuleEntity> moduleMap,
            Map<String, ActionEntity> actionMap,
            final Cache parameterCache,
            final AccessActionLogger accessActionLogger) {
        this.usePseudo = usePseudo;
        this.simpleParaManager = new SimpleParaManagerImpl();
        this.batchParaManager = new BatchParaManagerImpl();
        this.simpleMapManager = new SimpleMapManagerImpl();
        this.fileItemManager = new FileItemManagerImpl();
        this.batchMapManager = new BatchMapManagerImpl();
        this.pageParameterManager = new PageParameterManagerImpl();
        this.responseWriterManager = new ResponseWriterManagerImpl(this.pageParameterManager);
        this.httpServletRequestManager = httpServletRequestManager;
        this.httpServletResponseManager = new HttpServletResponseManagerImpl();
        this.excelDataManager = new ExcelDataManagerImpl();
        this.writeSessionManager = writeSessionManager;
        this.serviceWorkerMap = new HashMap<String, ServiceWorker>(256, 1);
        this.entityContextBuilder = entityContextBuilder;
        this.extendedEntityContextBuilder = extendedEntityContextBuilder;
        this.uploadFileManager = uploadFileManager;
        this.transactionSessionBeanLocalFactory = transactionSessionBeanLocalFactory;
        this.dictionaryContextBuilder = dictionaryContextBuilder;
        this.moduleMap = moduleMap;
        this.actionMap = actionMap;
        this.parameterCache = parameterCache;
        this.versionPrivilegeHandler = versionPrivilegeHandler;
        this.customPrivilegeHandler = customPrivilegeHandler;
        this.sessionEntityHandler = sessionEntityHandler;
        this.loginEmployeeInfoHandler = loginEmployeeInfoHandler;
        this.accessActionLogger = accessActionLogger;
    }

    @Override
    public UploadFileManager getUploadFileManager() {
        return this.uploadFileManager;
    }

    @Override
    public Map<String, ServiceWorker> getServiceWorkerMap() {
        return Collections.unmodifiableMap(this.serviceWorkerMap);
    }

    @Override
    public boolean assertExistServiceWorker(String actionName) {
        return this.serviceWorkerMap.containsKey(actionName);
    }

    @Override
    public EntityContextBuilder<T> getEntityContextBuilder() {
        return this.entityContextBuilder;
    }

    @Override
    public ExtendedEntityContextBuilder getExtendedEntityContextBuilder() {
        return this.extendedEntityContextBuilder;
    }

    @Override
    public DictionaryContextBuilder<T> getDictionaryContextBuilder() {
        return this.dictionaryContextBuilder;
    }

    @Override
    public FieldContextBuilder getFieldContextBuilder() {
        return this.entityContextBuilder.getFieldContextBuilder();
    }

    @Override
    public Map<String, ModuleEntity> getModuleMap() {
        return this.moduleMap;
    }

    @Override
    public Map<String, ActionEntity> getActionMap() {
        return this.actionMap;
    }

    @Override
    public VersionPrivilegeHandler getVersionPrivilegeHandler() {
        return this.versionPrivilegeHandler;
    }

    @Override
    public CustomPrivilegeHandler getCustomPrivilegeHandler() {
        return this.customPrivilegeHandler;
    }

    @Override
    public SessionEntityHandler getSessionEntityHandler() {
        return this.sessionEntityHandler;
    }

    @Override
    public LoginEmployeeInfoHandler getLoginEmployeeInfoHandler() {
        return this.loginEmployeeInfoHandler;
    }

    @Override
    public AccessActionLogger getAccessActionLogger() {
        return this.accessActionLogger;
    }
}
