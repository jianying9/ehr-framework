package com.ehr.sendmail.beans;

/**
 * 邮件发送用户设置实体类
 *
 * @author chancelin
 */
public class MailUserEntity {

    private long mailUserId;
    private String username;
    private String password;
    private String smtpHost;
    private boolean needSSL;
    private String desc;

    /**
     * 取得用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 取得用户密码
     *
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 取得发送邮件服务器
     *
     * @return SMTP服务器
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * 设置邮件发送服务器
     *
     * @param smtpHost SMTP服务器
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * 是否需要SSL登录
     *
     * @return true-是,false-否
     */
    public boolean isNeedSSL() {
        return needSSL;
    }

    /**
     * 设置是否需要SSL登录
     *
     * @param needSSL
     */
    public void setNeedSSL(boolean needSSL) {
        this.needSSL = needSSL;
    }

    public long getMailUserId() {
        return mailUserId;
    }

    public void setMailUserId(long mailUserId) {
        this.mailUserId = mailUserId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}