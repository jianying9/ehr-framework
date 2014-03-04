package com.ehr.framework.dao;

/**
 * 排序类型
 * @author zoe
 */
public enum OrderTypeEnum {

    ASC {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.ASC;
        }
    },
    DESC{

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.DESC;
        }
    };

    public abstract String getKeyWord();
}
