package com.ehr.sendmail;

import com.ehr.framework.logger.LogFactory;
import com.ehr.sendmail.beans.MailEntity;
import javax.mail.*;
import org.slf4j.Logger;

/**
 * 邮件发送类,实现同一个账号发送多封邮件,并回写发送状态到邮件实例中.
 *
 * @author chancelin
 */
public class SendMailInSession {

    /**
     * 邮件发送服务器地址的属性设置.
     */
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    /**
     * 邮件发送服务器是否需要验证的属性设置.
     */
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    /**
     * 邮件发送协议属性,SMTP协议.
     */
    private static final String SMTP_PROTOCOL = "smtp";
    /**
     * 邮件发送协议属性,SMTPS协议,即有SSL验证的协议
     */
    private static final String SMTPS_PROTOCOL = "smtps";
    /**
     *
     */
    private Session session; // 会话
    /**
     *
     */
    private Transport transport; // 发送邮件

    /**
     * 构造函数,初始化<code> Session, Transport</code>等.
     *
     * @param userName 用户名
     * @param password 密码
     * @param host 主机
     * @param useSSL 是否需要SSL验证
     */
    public SendMailInSession(String userName, String password, String host, boolean useSSL) {
        init(userName, password, host, useSSL);
    }

    /**
     * 初始化
     *
     * @param userName 用户名
     * @param password 密码
     * @param host 主机
     * @param useSSL 是否需要SSL验证
     */
    private void init(final String userName, final String password, String host, boolean useSSL) {
        String protocol = useSSL ? SMTPS_PROTOCOL : SMTP_PROTOCOL;
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证
        Authenticator sa = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        // 需要经过授权
        System.getProperties().put(MAIL_SMTP_AUTH, "true");
        // 设置发送邮件的邮件服务器的属性
        System.getProperties().put(MAIL_SMTP_HOST, host);
        // 用刚刚设置好的props对象构建一个session
        session = Session.getInstance(System.getProperties(), sa);
        try {
            transport = session.getTransport(protocol);
            // 连接服务器的邮箱
            transport.connect(host, userName, password);
        } catch (MessagingException e) {
            Logger logger = LogFactory.getFrameworkLogger();
            logger.error("init mail session error", e);
        }
    }

    /**
     * 发送邮件，包含：邮件正文、（多个附件）等,同时发送成功会设置{@link MailEntity}的status属性为
     * {@link MailEntity#STATUS_SUCCESS},<BR> 发送失败会设置{@link MailEntity}的status属性为{@link MailEntity#STATUS_FAIL}<br>
     *
     * @param mail 邮件实例
     * @return 是否成功
     */
    public boolean send(MailEntity mail) {
        //logger.debug("发送邮件:"+mail);
        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(Boolean.valueOf(System.getProperty("MAIL_SESSION_DEBUG")));
        boolean success = false;
        try {
            Message msg = mail.getMessage(session);
            // 发送一封邮件
            transport.sendMessage(msg, msg.getAllRecipients());
            success = true;
        } catch (MessagingException e) {
            Logger logger = LogFactory.getFrameworkLogger();
            logger.error("send mail error", e);
        } finally {
            if (null != transport) {
                try {
                    transport.close();
                } catch (Exception e) {
                }
            }
        }
        return success;
    }
}