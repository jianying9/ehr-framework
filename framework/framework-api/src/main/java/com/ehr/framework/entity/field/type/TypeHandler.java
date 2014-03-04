package com.ehr.framework.entity.field.type;

import com.ehr.framework.entity.field.FieldTypeClassEnum;

/**
 * 类型处理类
 * @author zoe
 */
public interface TypeHandler {

    public String DEFAULT_CHAR_VALUE = "";
    public String DEFAULT_NUMBER_VALUE = "-1";
    public String DEFAULT_DECIMAL_VALUE = "0";
    public String DEFAULT_DATE_VALUE = "1970-01-01 00:00:00";

    public String validate(String value);

    public String getDefaultValue();

    public FieldTypeClassEnum getFieldTypeClassEnum();

    /**
     * 如果值为null，数字类型格式化返回-1，字符串类型返回“”，时间类型返回1970-01-01 00:00:00
     * @param value
     * @return
     */
    public String formatValue(String value);
    
    public String getRandomValue();
}
