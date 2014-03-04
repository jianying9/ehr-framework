package com.ehr.framework.session;

/**
 *
 * @author zoe
 */
public interface SessionEntityHandler {

    public SessionEntity inquireSessionByLoginCode(String loginCode, String entry, String t);
}
