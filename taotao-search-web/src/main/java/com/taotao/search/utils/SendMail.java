package com.taotao.search.utils;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//163邮箱,邮件发送工具类
public class SendMail {

    public static void sendEmail(String subject,String text) throws MessagingException {
        Properties properties=new Properties();
        //设置访问smtp服务器需要访问
        properties.setProperty("mail.smtp.auth", "true");
        //设置访问服务器协议
        properties.setProperty("mail.transport.protocol", "smtp");

        Session session=Session.getDefaultInstance(properties);
        //打开debug功能
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        //设置163邮箱用户名
        message.setFrom(new InternetAddress("fyt9330@163.com"));
        //设置邮件主题
        message.setSubject(subject);
        //设置邮件内容
        message.setText(text);

        Transport trans=session.getTransport();
        //以下四个参数,前两个可以认为固定.后两个参数为用户名及客户端授权码
        trans.connect("smtp.163.com", 25, "fyt9330@163.com", "fyt931127");
        //要发送的邮箱
        trans.sendMessage(message, new Address[]{new InternetAddress("965621112@qq.com")});

        //关闭链接
        trans.close();
    }
}
