package com.ehr.framework.service.accesslogger;

import com.ehr.framework.logger.AccessActionLog;
import com.ehr.framework.logger.AccessActionLogger;
import com.ehr.framework.logger.LogFactory;
import org.slf4j.Logger;

/**
 * User: zoe
 * Date: 8/13/12
 * Time: 1:53 PM
 */
public class DefaultAccessLogger implements AccessActionLogger {
    public void insertAccessActionLog(AccessActionLog accessActionLog) {
        Logger logger = LogFactory.getFrameworkLogger();
        logger.debug("access action: {}", accessActionLog.getAction());
    }
}
