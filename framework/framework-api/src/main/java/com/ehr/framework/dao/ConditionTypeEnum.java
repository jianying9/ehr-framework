package com.ehr.framework.dao;

/**
 * 条件类型
 * @author zoe
 */
public enum ConditionTypeEnum {

    //=
    EQUAL {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.EQUAL;
        }
    },
    //<>
    NOT_EQUAL {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.NOT_EQUAL;
        }
    },
    //LIKE %value%
    LIKE {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.LIKE;
        }
    },
    //LIKE value%
    HEAD_AND_LIKE {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.LIKE;
        }
    },
    //LIKE %value
    END_AND_LIKE {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.LIKE;
        }
    },
    //>
    GREATER {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.GREATER;
        }
    },
    //<
    LESS {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.LESS;
        }
    },
    //>=
    EQUAL_AND_GREATER {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.EQUAL_AND_GREATER;
        }
    },
    //<=
    EQUAL_AND_LESS {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.EQUAL_AND_LESS;
        }
    },
    //<=
    IN {

        @Override
        public String getKeyWord() {
            return SqlBuilderConfig.IN;
        }
    };

    public abstract String getKeyWord();
}
