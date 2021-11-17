package org.toolkit.communication.email.util;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Aliyun服务邮件发送工具包
 *
 * @author: hanyun.li
 * @date: 2021/11/17
 **/
@Slf4j
public class EmailUtil {

    /**
     * aliyun主机名称
     */
    private static final String SMTP_HOST = "smtpdm.aliyun.com";

    /**
     * 服务器端口
     */
    private static final String SMTP_PORT = "80";

    /**
     * 密码
     */
    private static final String PASSWORD = "A1B2C3d4e5";

    /**
     * 用户名
     */
    private static final String EMAIL_USERNAME = "javatest@edm.vision-iot.co";

    /**
     * 发送邮件
     *
     * @param receivingAddress 收件人邮件地址（单个）
     * @param emailContent     发送邮件内容（文本）
     * @param emailTitle       发送邮件的标题
     */
    public static void sendMail(String receivingAddress, String emailContent, String emailTitle) {
        final List<String> emails = new ArrayList<>(1);
        emails.add(receivingAddress);
        sendMail(emails, emailContent, emailTitle);
    }

    /**
     * 发送邮件
     *
     * @param receivingAddresses 收件人邮件地址（多个）
     * @param emailContent       发送邮件内容（文本）
     * @param emailTitle         发送邮件的标题
     */
    public static void sendMail(List<String> receivingAddresses, String emailContent, String emailTitle) {
        // 配置发送邮件的环境属性
        String[] emails = receivingAddresses.toArray(new String[0]);
        // 获取验证信息
        MimeMessage message = getMimeMessage(emailTitle, emails);

        try {
            // 设置邮件的内容体
            message.setContent(emailContent, "text/html;charset=UTF-8");
            message.saveChanges();
            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            log.error("发送邮件错误, 邮件地址：" + receivingAddresses, e);
        }
    }

    /**
     * 获取验证信息（实际赋值属性信息）
     *
     * @param title
     * @param address
     * @return 认证之后的
     */
    private static MimeMessage getMimeMessage(String title, String... address) {
        // 表示SMTP发送邮件，需要进行身份验证
        final Properties props = new Properties();
        // 开启smtp认证
        props.put("mail.smtp.auth", "true");
        // 服务主机地址
        props.put("mail.smtp.host", SMTP_HOST);
        // 服务器端口
        props.put("mail.smtp.port", SMTP_PORT);
        // 发件人的账号
        props.put("mail.user", EMAIL_USERNAME);
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", PASSWORD);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                return new PasswordAuthentication(userName, PASSWORD);
            }
        };

        // 使用环境属性和授权信息，创建邮件会话
        try {
            Session mailSession = Session.getInstance(props, authenticator);
            mailSession.setDebug(false);

            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(EMAIL_USERNAME);
            message.setFrom(form);
            // 设置收件人
            for (String toEmail : address) {
                message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
            }

            // 设置邮件标题
            message.setSubject(title);
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
