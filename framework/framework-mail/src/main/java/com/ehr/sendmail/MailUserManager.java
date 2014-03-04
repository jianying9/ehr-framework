package com.ehr.sendmail;


import com.ehr.sendmail.beans.MailUserEntity;

public class MailUserManager {

    private static final int MAIL_USER_ID = 1;
    private static final String MAIL_USER = "account@91yong.com";
    private static final String MAIL_PASSWD = "ehr.nd.com&zl";
    private static final String MAIL_SMTP = "mail.91yong.com";
    private static final boolean MAIL_SSL = false;
    private static final String MAIL_DESC = "91eHR";

    public static MailUserEntity getMailUserEntity() {
        MailUserEntity entity = new MailUserEntity();
        entity.setMailUserId(MAIL_USER_ID);
        entity.setUsername(MAIL_USER);
        entity.setPassword(MAIL_PASSWD);
        entity.setSmtpHost(MAIL_SMTP);
        entity.setNeedSSL(MAIL_SSL);
        entity.setDesc(MAIL_DESC);
        return entity;
    }
}