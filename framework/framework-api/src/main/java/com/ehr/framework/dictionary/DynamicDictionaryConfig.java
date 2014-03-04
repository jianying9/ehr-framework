package com.ehr.framework.dictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户描述实现动态数据字典的接口
 * @author zoe
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DynamicDictionaryConfig {

    /**
     * 动态数据字典类型
     * @return 
     */
    public String dynamicDictionaryName();
}
