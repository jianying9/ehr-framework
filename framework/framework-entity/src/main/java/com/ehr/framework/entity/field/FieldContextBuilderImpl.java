package com.ehr.framework.entity.field;

import com.ehr.framework.dictionary.DictionaryManager;
import com.ehr.framework.entity.field.filter.FilterFactory;
import com.ehr.framework.entity.field.filter.FilterFactoryImpl;
import com.ehr.framework.entity.field.type.TypeHandlerFactory;
import com.ehr.framework.entity.field.type.TypeHandlerFactoryImpl;
import com.ehr.framework.worker.workhandler.WriteDynamicDictionaryManager;

/**
 * 全局信息构造类
 * @author zoe
 */
public class FieldContextBuilderImpl implements FieldContextBuilder {

    private final FilterFactory filterFactory;

    @Override
    public final FilterFactory getFilterFactory() {
        return this.filterFactory;
    }
    private final TypeHandlerFactory typeHandlerFactory;

    @Override
    public final TypeHandlerFactory getTypeHandlerFactory() {
        return this.typeHandlerFactory;
    }
    private final DictionaryManager dictionaryManager;

    @Override
    public final DictionaryManager getDictionaryManager() {
        return this.dictionaryManager;
    }
    private final WriteDynamicDictionaryManager writeDynamicDictionaryManager;

    @Override
    public final WriteDynamicDictionaryManager getWriteDynamicDictionaryManager() {
        return this.writeDynamicDictionaryManager;
    }

    /**
     * 构造函数
     * @param properties 
     */
    public FieldContextBuilderImpl(final DictionaryManager dictionaryManager, final WriteDynamicDictionaryManager writeDynamicDictionaryManager) {
        this.filterFactory = new FilterFactoryImpl();
        this.typeHandlerFactory = new TypeHandlerFactoryImpl();
        this.dictionaryManager = dictionaryManager;
        this.writeDynamicDictionaryManager = writeDynamicDictionaryManager;
    }
}
