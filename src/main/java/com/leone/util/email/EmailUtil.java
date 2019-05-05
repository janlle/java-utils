package com.leone.util.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * <p>邮件发送工具
 * <p>
 * 1．SMTP（递送邮件机制）
 * 简单邮件传输协议 SMTP 服务器将邮件转发到接收者的SMTP服务器，直至最后被接收者通过POP或者IMAP协议获取。
 * <p>
 * 2．POP（获取邮件机制）
 * 邮局协议，目前为第3个版本POP3
 * <p>
 * 3．IMAP（多目录共享）
 * 接收信息的高级协议，目前版本为第4版 IMAP4 接收新信息，将这些信息递送给用户，维护每个用户的多个目录。
 * <p>
 * 4．MIME
 * 邮件扩展内容格式：信息格式、附件格式等等
 * <p>
 * 5．NNTP
 * 第三方新闻组协议
 *
 * @author leone
 * @since 2019-04-26
 **/
public class EmailUtil {

    /**
     * 发送邮件
     *
     * @param email 邮件信息
     */
    public static void sendEmail(Email email) {
        // 邮件配置信息
        Properties props = email.getProps();

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getUsername(), email.getPassword());
            }
        };

        // 根据属性新建一个邮件会话
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(false);
        List<DataSource> attachments = email.getAttachments();
        // 由邮件会话新建一个消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(email.getSender()));
            // 设置收件人,并设置其接收类型为TO
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
            message.setSubject(email.getTitle());
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart("mixed");
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(email.getContent(), "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);

            // 设置附件
            for (DataSource attach : attachments) {
                BodyPart msgAttach = new MimeBodyPart();
                msgAttach.setDataHandler(new DataHandler(attach));
                msgAttach.setFileName(MimeUtility.encodeText(attach.getName()));
                multipart.addBodyPart(msgAttach);
            }

            message.setContent(multipart);
            message.setSentDate(new Date());
            message.saveChanges();
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
