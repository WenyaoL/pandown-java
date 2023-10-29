package com.pan.pandown.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-08
 */
@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送验证码Email并返回生成的验证码
     * @param email
     * @return
     * @throws MessagingException
     */
    public String captchaEmail(String email) throws MessagingException {

        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        //谁发
        minehelper.setFrom(from);
        //谁要接收
        minehelper.setTo(email);
        //邮件主题
        minehelper.setSubject("pandown-java验证码");

        //生成验证码[100000-999999]
        int number = new Random().nextInt(899999) + 100000;
        String captcha = String.valueOf(number);
        //邮件内容   true 表示带有附件或html
        minehelper.setText(HTMLService.getCaptchaHtml(captcha), true);

        mailSender.send(message);

        return captcha;
    }

    /**
     * 发送验证码Email并返回生成的验证码
     * @param email
     * @return
     * @throws MessagingException
     */
    public String captchaEmailOfForgetPwd(String email) throws MessagingException {

        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        //谁发
        minehelper.setFrom(from);
        //谁要接收
        minehelper.setTo(email);
        //邮件主题
        minehelper.setSubject("pandown-java验证码");

        //生成验证码[100000-999999]
        int number = new Random().nextInt(899999) + 100000;
        String captcha = String.valueOf(number);
        //邮件内容   true 表示带有附件或html
        minehelper.setText(HTMLService.getCaptchaHtml_forgetPwd(captcha), true);

        mailSender.send(message);

        return captcha;
    }

}
