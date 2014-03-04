package com.ehr.framework.entity.field;

import com.ehr.framework.entity.field.type.FieldTypeEnum;

/**
 *
 * @author zoe
 */
public interface FieldHandler {

    public String getName();

    public String getExportName();

    public String getJson(String value);

    public String getJsonNoDictionary(String value);

    public String getExcelCellValue(String value);

    public String getDictionaryKey(String eleNames);

    public String validate(String value);
    
    public String formatValue(String value);
    
    public String getDefaultValue();
    
    public FieldTypeEnum getFieldType();
    
    public String dynamicDictionaryName();
    
    public String getRandomValue();
}
