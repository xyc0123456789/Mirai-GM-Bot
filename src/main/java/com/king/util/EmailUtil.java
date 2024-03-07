package com.king.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    public static void main(String[] args) {
        String to = "*@qq.com";
        String from = "*@outlook.com";
        String host = "smtp.office365.com";
        String port = "587";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("*@outlook.com", "*");
            }
        };
        Session session = Session.getInstance(properties, authenticator);


        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("这是邮件主题");

            message.setText("这是邮件内容");

            Transport.send(message);
            System.out.println("邮件已发送");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
