package com.mybatisplus_comp3334.service;

import com.mybatisplus_comp3334.service.concept.MailService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Log
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sentSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(content);
        message.setText(content);
        mailSender.send(message);
        log.info("send a mail to "+ to);
    }
}
