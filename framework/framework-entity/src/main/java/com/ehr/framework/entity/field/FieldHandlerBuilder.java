package com.ehr.framework.entity.field;

import com.ehr.framework.context.DynamicDictionaryManager;
import com.ehr.framework.dictionary.DictionaryManager;
import com.ehr.framework.entity.field.filter.Filter;
import com.ehr.framework.entity.field.filter.FilterFactory;
import com.ehr.framework.entity.field.filter.FilterTypeEnum;
import com.ehr.framework.entity.field.type.FieldTypeEnum;
import com.ehr.framework.entity.field.type.TypeHandler;
import com.ehr.framework.entity.field.type.TypeHandlerFactory;
import java.util.Map;

/**
 *
 * @author zoe
 */
public class FieldHandlerBuilder {

    private final String fieldName;
    private final FieldConfig fieldConfig;
    private final TypeHandlerFactory typeHandlerFcatory;
    private final DictionaryManager dictionaryManager;
    private final DynamicDictionaryManager dynamicDictionaryManager;
    private final FilterFactory filterFactory;
    private final String reservedWord = "|act|cacheId|returnNames|noPseudo|loginCode|loginEmpId|entry|seed|";

    public FieldHandlerBuilder(final FieldContextBuilder appCtxBuilder, final String fieldName, final FieldConfig fieldConfig) {
        this.fieldName = fieldName;
        this.fieldConfig = fieldConfig;
        this.filterFactory = appCtxBuilder.getFilterFactory();
        this.typeHandlerFcatory = appCtxBuilder.getTypeHandlerFactory();
        this.dictionaryManager = appCtxBuilder.getDictionaryManager();
        this.dynamicDictionaryManager = appCtxBuilder.getWriteDynamicDictionaryManager();
    }

    public FieldHandler build() {
        FieldHandler fieldHandler = null;
        //保留字验证
        StringBuilder validateBuilder = new StringBuilder(fieldName.length() + 2);
        validateBuilder.append('|').append(this.fieldName).append('|');
        if (this.reservedWord.indexOf(validateBuilder.toString()) > -1) {
            throw new RuntimeException("There was an error building FieldHandler. Cause: reserved word : ".concat(fieldName));
        }
        //1.获取类型
        FieldTypeEnum fieldTypeEnum = this.fieldConfig.type();
        TypeHandler typeHandler = this.typeHandlerFcatory.getTypeHandler(fieldTypeEnum);
        if (typeHandler == null) {
            throw new RuntimeException("There was an error building FieldHandler. Cause: could not find TypeHandler.");
        }
        //2.获取描述
        String fieldDesc = this.fieldConfig.fieldDesc();
        //3.获取导出名称,如果导出名称为空字符串，则默认为字段描述
        String exportName = this.fieldConfig.exportName();
        if (exportName.isEmpty()) {
            exportName = fieldDesc;
        }
        //4.获取过滤对象
        FilterTypeEnum[] filterTypeEnums = this.fieldConfig.filterTypes();
        Filter[] filters = new Filter[0];
        if (filterTypeEnums.length > 0) {
            Filter filter;
            filters = new Filter[filterTypeEnums.length];
            for (int index = 0; index < filterTypeEnums.length; index++) {
                filter = this.filterFactory.getFilter(filterTypeEnums[index]);
                if (filter == null) {
                    throw new RuntimeException("There was an error building FieldHandler. Cause: could not find Filter.");
                }
                filters[index] = filter;
            }
        }
        final String defaultValue = fieldConfig.defaultValue();
        //5.创建处理对象
        //判断是否json数组
        if (this.fieldConfig.jsonArray()) {
            //5.1是json数组
            String jsonArrayTemp =  this.fieldConfig.jsonArrayTemp();
            fieldHandler = new JsonArrayFieldHandlerImpl(this.fieldName, exportName, typeHandler, fieldTypeEnum, defaultValue, jsonArrayTemp);
        } else {
            //5.2非json数组
            //判断是否是数据字典类型
            final String dictionaryGroup = this.fieldConfig.dictionaryGroup();
            if (dictionaryGroup.isEmpty()) {
                //非静态数据字典
                final String dynamicDictionaryName = this.fieldConfig.dynamicDictionaryName();
                if (!dynamicDictionaryName.isEmpty()) {
                    //动态数据字典
                    fieldHandler = new DynamicDictionaryFieldHandlerImpl(this.fieldName, exportName, filters, dynamicDictionaryName, this.dynamicDictionaryManager, typeHandler, fieldTypeEnum, defaultValue);
                } else {
                    //5.2.1非数据字典类型
                    //判断类型是否数字类型、时间类型还是字符类型
                    switch (typeHandler.getFieldTypeClassEnum()) {
                        case NUMBER:
                            fieldHandler = new NumberFieldHandlerImpl(this.fieldName, exportName, typeHandler, fieldTypeEnum, defaultValue);
                            break;
                        case DATE:
                            fieldHandler = new DateFieldHandlerImpl(this.fieldName, exportName, typeHandler, fieldTypeEnum, defaultValue);
                            break;
                        case STRING:
                            fieldHandler = new StringFieldHandlerImpl(this.fieldName, exportName, filters, typeHandler, fieldTypeEnum, defaultValue);
                    }
                }
            } else {
                //5.2.2静态数据字典类型
                //判断是否是数据字典集合
                Map<String, String> dictionaryMap = this.dictionaryManager.getDictionaryMap(dictionaryGroup);
                if (dictionaryMap == null) {
                    throw new RuntimeException("There was an error building FieldHandler. Cause: can not find dictionaryGroup ".concat(dictionaryGroup));
                }
                if (this.fieldConfig.dictionaryArray()) {
                    //为数据字典集合类型
                    fieldHandler = new DictionaryArrayFieldHandlerImpl(this.fieldName, exportName, dictionaryMap, typeHandler, fieldTypeEnum, defaultValue);
                } else {
                    //单数据字典类型
                    fieldHandler = new DictionaryFieldHandlerImpl(this.fieldName, exportName, dictionaryMap, typeHandler, fieldTypeEnum, defaultValue);
                }
            }
        }
        return fieldHandler;
    }
}
