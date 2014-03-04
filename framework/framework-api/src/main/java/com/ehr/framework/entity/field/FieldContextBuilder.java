package com.ehr.framework.entity.field;

import com.ehr.framework.dictionary.DictionaryManager;
import com.ehr.framework.entity.field.filter.FilterFactory;
import com.ehr.framework.entity.field.type.TypeHandlerFactory;
import com.ehr.framework.worker.workhandler.WriteDynamicDictionaryManager;

/**
 *
 * @author zoe
 */
public interface FieldContextBuilder {

    public FilterFactory getFilterFactory();

    public TypeHandlerFactory getTypeHandlerFactory();

    public DictionaryManager getDictionaryManager();

    public WriteDynamicDictionaryManager getWriteDynamicDictionaryManager();
}
