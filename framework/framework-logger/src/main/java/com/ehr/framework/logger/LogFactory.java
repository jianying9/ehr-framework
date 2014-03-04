package com.ehr.framework.logger;

import com.ehr.framework.config.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据LoggerNameEnum枚举,获取对应的logger对象.在使用之前,请先在LoggerNameEnum中维护每个logger的枚举,以及该枚举对应的name
 * @author zoe
 */
public final class LogFactory {

    private final static LoggerType frameworkLogger = new FrameworkLoggerImpl();

    /**
     * 根据LoggerNameEnum获取对应的logger对象
     * @param loggerNameEnum
     * @return
     */
    public static Logger getLogger(LoggerType loggerType) {
        return LoggerFactory.getLogger("java.util.logging.ConsoleHandler.".concat(loggerType.getLoggerName()));
    }

    public static Logger getFrameworkLogger() {
        return LogFactory.getLogger(LogFactory.frameworkLogger);
    }
}
