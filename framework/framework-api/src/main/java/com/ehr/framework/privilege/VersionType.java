package com.ehr.framework.privilege;

/**
 *
 * @author zoe
 */
public final class VersionType {

    //继承
    public final static int EXTEND = -10;
    //无版本控制
    public final static int ALL = -1;
    //未激活
    public final static int NOT_ACTIVATE = 1501;
    //已激活
    public final static int ACTIVATE = 1511;
    //专业
    public final static int PROFESSION = 1521;
    //企业
    public final static int ENTERPRISE = 1531;
    //企业+专业
    public final static int ENTERPRISE_PROFESSION = 1541;
}
