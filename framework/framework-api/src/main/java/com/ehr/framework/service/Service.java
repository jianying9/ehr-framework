package com.ehr.framework.service;

import com.ehr.framework.worker.workhandler.WorkHandler;

/**
 *
 * @author zoe
 */
public interface Service extends WorkHandler{

    @Override
    public void execute();
}
