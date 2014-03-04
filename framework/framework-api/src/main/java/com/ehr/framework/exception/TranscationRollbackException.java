package com.ehr.framework.exception;

import com.ehr.framework.config.ResponseFlagType;

/**
 *
 * @author zoe
 */
public class TranscationRollbackException extends RuntimeException {

    private static final long serialVersionUID = -7606526262000673005L;
    private final ResponseFlagType responseFlagType;
    private final String info;

    public TranscationRollbackException(final ResponseFlagType responseFlagType) {
        super(responseFlagType.getFlagName());
        this.responseFlagType = responseFlagType;
        this.info = "";
    }

    public TranscationRollbackException(final ResponseFlagType responseFlagType, String info) {
        super(responseFlagType.getFlagName());
        this.responseFlagType = responseFlagType;
        this.info = info;
    }

    public ResponseFlagType getFlagName() {
        return this.responseFlagType;
    }

    public String getInfo() {
        return info;
    }
}
