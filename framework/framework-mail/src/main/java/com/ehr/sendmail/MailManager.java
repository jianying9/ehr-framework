package com.ehr.sendmail;

import com.ehr.sendmail.beans.MailAttachmentEntity;
import com.ehr.sendmail.beans.MailEntity;
import com.ehr.sendmail.beans.MailUserEntity;
import java.util.Date;

/**
 * 邮件管理操作类
 *
 * @author chancelin
 */
public class MailManager {

    /**
     * 发送邮件,可以抄送
     *
     * @param to 接收人邮箱
     * @param title 邮件名称
     * @param content 邮件内容
     * @param isMime 是否包含HTML
     * @param cc 邮件抄送人
     * @param bcc 邮件暗抄送人
     * @param mm 附件
     */
    public static void sendMail(String to, String title, String content, boolean isMime, String cc, String bcc, MailAttachmentEntity mm) {
        MailEntity mailEntity = new MailEntity();
        mailEntity.setMailTo(to);
        mailEntity.setMailCc(cc);
        mailEntity.setMailBcc(bcc);
        mailEntity.setMime(isMime);
        mailEntity.setSendTime(new Date());
        mailEntity.setTitle(title);
        mailEntity.setContent(content);
        mailEntity.setMailAttachmentEntity(mm);
        MailUserEntity mailUser = MailUserManager.getMailUserEntity();
        SendMailInSession sendmail = new SendMailInSession(mailUser.getUsername(), mailUser.getPassword(), mailUser.getSmtpHost(),
                mailUser.isNeedSSL());
        String senderEmail;
        String desc = mailUser.getDesc();
        String userName = mailUser.getUsername();
        if (desc != null && desc.length() > 0) {
            StringBuilder senderEmailBuilder = new StringBuilder(userName.length() + desc.length() + 6);
            senderEmailBuilder.append('"').append(desc).append("\"<").append(userName).append('>');
            senderEmail = senderEmailBuilder.toString();
        } else {
            senderEmail = userName;
        }
        mailEntity.setSenderEmail(senderEmail);
        sendmail.send(mailEntity);
    }

    /**
     * 发送邮件,可以抄送
     *
     * @param to 接收人邮箱
     * @param title 邮件名称
     * @param content 邮件内容
     * @param isMime 是否包含HTML
     * @param cc 邮件抄送人
     * @param bcc 邮件暗抄送人
     */
    public static void sendMail(String to, String title, String content, boolean isMime, String cc, String bcc) {
        MailManager.sendMail(to, title, content, isMime, cc, bcc, null);
    }
    
    @Deprecated
    public static void sendMail(int userId, String to, String title, String content, boolean isMime, String cc, String bcc) {
        MailManager.sendMail(to, title, content, isMime, cc, bcc, null);
    }

    /**
     * 发送邮件，没有抄送
     *
     * @param to 接收人邮箱
     * @param title 邮件名称
     * @param content 邮件内容
     * @param isMime 是否包含HTML
     */
    public static void sendMail(String to, String title, String content, boolean isMime) {
        MailManager.sendMail(to, title, content, isMime, null, null, null);
    }

    /**
     * 发送邮件，没有抄送，带附件
     *
     * @param to 接收人邮箱
     * @param title 邮件名称
     * @param content 邮件内容
     * @param isMime 是否包含HTML
     * @param mm 附件
     */
    public static void sendMail(String to, String title, String content, boolean isMime, MailAttachmentEntity mm) {
        MailManager.sendMail(to, title, content, isMime, null, null, mm);
    }
}