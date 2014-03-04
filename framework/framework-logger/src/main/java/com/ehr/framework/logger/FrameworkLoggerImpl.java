package com.ehr.framework.logger;

import com.ehr.framework.config.LoggerType;

/**
 *
 * @author zoe
 */
public class FrameworkLoggerImpl implements LoggerType {

    @Override
    public String getLoggerName() {
        return "Framework";
    }
}
