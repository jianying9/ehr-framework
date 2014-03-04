package com.ehr.sendmail.beans;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件实体类
 *
 * @author chancelin
 */
public class MailEntity {

    private long mailId;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String title;
    private String content;
    private boolean isMime;
    private Date sendTime;
    private String senderEmail;
    private MailAttachmentEntity mailAttachmentEntity;

    /**
     * 无参构造函数,初始化实例.
     */
    public MailEntity() {
        isMime = false;
    }

    /**
     * 邮件附件实体
     *
     * @param MailAttachmentEntity 附件实体
     */
    public void setMailAttachmentEntity(MailAttachmentEntity mailAttachmentEntity) {
        this.mailAttachmentEntity = mailAttachmentEntity;
    }

    /**
     * 取得邮件序号
     *
     * @return 邮件序号
     */
    public long getMailId() {
        return mailId;
    }

    /**
     * 设置邮件序号
     *
     * @param mailId 邮件序号
     */
    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    /**
     * 取得邮件接收人,可多个,用逗号隔开
     *
     * @return 邮件接收人
     */
    public String getMailTo() {
        return mailTo;
    }

    /**
     * 设置邮件接收人
     *
     * @param mailTo 邮件接收人
     */
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    /**
     * 取得邮件抄送人,可多个,用逗号隔开
     *
     * @return 邮件抄送人
     */
    public String getMailCc() {
        return mailCc != null ? mailCc : "";
    }

    /**
     * 设置邮件抄送人
     *
     * @param mailCc 邮件抄送人
     */
    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    /**
     * 取得邮件暗抄送人,可多个,用逗号隔开
     *
     * @return 邮件暗抄送人
     */
    public String getMailBcc() {
        return mailBcc != null ? mailBcc : "";
    }

    /**
     * 设置邮件暗抄送人
     *
     * @param mailBcc 邮件暗抄送人
     */
    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
    }

    /**
     * 取得邮件标题
     *
     * @return 邮件标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置邮件标题
     *
     * @param title 邮件标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 取得邮件内容
     *
     * @return 邮件内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件内容
     *
     * @param content 邮件内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 邮件内容是否HTML格式
     *
     * @return true-是,false-否
     */
    public boolean isMime() {
        return isMime;
    }

    /**
     * 设置邮件内容是否HTML格式
     *
     * @param isMime true-是,false-否
     */
    public void setMime(boolean isMime) {
        this.isMime = isMime;
    }

    /**
     * 取得发送时间
     *
     * @return 发送时间
     */
    public Date getSendTime() {
        return sendTime == null ? new Date() : sendTime;
    }

    /**
     * 设置发送事件
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 取得发送人邮件地址
     *
     * @return 邮件地址
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * 取得附件实体
     *
     * @param mailAttachmentEntity 附件
     */
    public MailAttachmentEntity getMailAttachmentEntity() {
        return mailAttachmentEntity;
    }

    /**
     * 设置发送人邮件地址
     *
     * @param senderEmail email
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * 生成JavaMail邮件消息,该消息可由javamail发送给服务器.
     *
     * @param session javamail会话
     * @return JavaMail邮件消息
     * @throws MessagingException 生成消息异常
     */
    public Message getMessage(Session session) throws MessagingException {
        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        // 加载发件人地址
        message.setFrom(new InternetAddress(senderEmail));
        message.setSentDate(getSendTime());
        // 加载收件人地址
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        if (mailCc != null) {
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCc));
        }
        if (mailBcc != null) {
            message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailBcc));
        }
        // 加载标题
        message.setSubject(title);
        if ((content != null && content.length() > 0)) {
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart("mixed");
            if (content != null && content.length() > 0) {
                // 设置邮件的文本内容
                MimeBodyPart contentPart = new MimeBodyPart();
                if (isMime) {
                    Multipart bodyMultipart = new MimeMultipart("related");
                    contentPart.setContent(bodyMultipart);
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    bodyMultipart.addBodyPart(htmlPart);
                    htmlPart.setContent(content, "text/html;charset=utf-8");
                } else {
                    contentPart.setText(content);
                }
                multipart.addBodyPart(contentPart);
            }
            //添加附件
            if (mailAttachmentEntity != null) {
                BodyPart mdp;
                DataHandler dh;
                String fileName;
                List<FileDataSource> fileDataSourceList = mailAttachmentEntity.getFileDataSourceList();
                if (!fileDataSourceList.isEmpty()) {
                    for (FileDataSource fileDataSource : fileDataSourceList) {
                        mdp = new MimeBodyPart();
                        dh = new DataHandler(fileDataSource);
                        fileName = fileDataSource.getName();
                        try {
                            fileName = MimeUtility.encodeWord(fileName);
                        } catch (UnsupportedEncodingException ex) {
                        }
                        mdp.setFileName(fileName);
                        mdp.setDataHandler(dh);
                        multipart.addBodyPart(mdp);
                    }
                }
            }
            message.setContent(multipart);
        }
        // 保存邮件
        message.saveChanges();
        return message;
    }
}