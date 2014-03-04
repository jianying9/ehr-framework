package com.ehr.framework.worker;

import com.ehr.framework.context.TransactionSessionBeanLocalFactory;
import com.ehr.framework.dao.Entity;
import com.ehr.framework.dictionary.DictionaryContextBuilder;
import com.ehr.framework.entity.EntityContextBuilder;
import com.ehr.framework.entity.ExtendedEntityContextBuilder;
import com.ehr.framework.entity.field.FieldContextBuilder;
import com.ehr.framework.file.UploadFileManager;
import com.ehr.framework.logger.AccessActionLogger;
import com.ehr.framework.privilege.ActionEntity;
import com.ehr.framework.privilege.CustomPrivilegeHandler;
import com.ehr.framework.privilege.ModuleEntity;
import com.ehr.framework.privilege.VersionPrivilegeHandler;
import com.ehr.framework.session.SessionEntityHandler;
import com.ehr.framework.worker.workhandler.*;
import java.util.Map;
import net.sf.ehcache.Cache;

/**
 *
 * @author zoe
 */
public interface ServiceWorkerContextBuilder<T extends Entity> {

    public void putServiceWorker(final String actionName, final ServiceWorker serviceWorker, String className);

    public Cache getParameterCache();

    public Map<String, ServiceWorker> getServiceWorkerMap();

    public TransactionSessionBeanLocalFactory getTransactionSessionBeanLocalFactory();

    public WritePageParameterManager getPageParameterManager();

    public HttpServletRequestManager getHttpServletRequestManager();

    public HttpServletResponseManager getHttpServletResponseManager();

    public ResponseWriterManager getResponseWriterManager();

    public WriteSimpleParaManager getSimpleParaManager();

    public WriteBatchParaManager getBatchParaManager();

    public WriteSimpleMapManager getSimpleMapManager();

    public WriteBatchMapManager getBatchMapManager();

    public WriteFileItemManager getFileItemManager();

    public WriteExcelDataManager getExcelDataManager();

    public UploadFileManager getUploadFileManager();

    public WriteSessionManager getWriteSessionManager();

    public boolean assertExistServiceWorker(final String actionName);

    public EntityContextBuilder<T> getEntityContextBuilder();

    public ExtendedEntityContextBuilder getExtendedEntityContextBuilder();

    public DictionaryContextBuilder<T> getDictionaryContextBuilder();

    public FieldContextBuilder getFieldContextBuilder();

    public Map<String, ModuleEntity> getModuleMap();

    public Map<String, ActionEntity> getActionMap();

    public boolean isUsePseudo();

    public VersionPrivilegeHandler getVersionPrivilegeHandler();

    public CustomPrivilegeHandler getCustomPrivilegeHandler();

    public SessionEntityHandler getSessionEntityHandler();

    public LoginEmployeeInfoHandler getLoginEmployeeInfoHandler();
    
    public AccessActionLogger getAccessActionLogger();
}
