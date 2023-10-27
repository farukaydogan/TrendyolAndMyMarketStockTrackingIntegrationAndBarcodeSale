package com.fta.stock.tracking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String errorMessage,String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("info@farukaydogan.com"); // Bu, kimlik doğrulaması yapılan kullanıcı ile eşleşmelidir.
        helper.setTo("faruk034595@gmail.com");
        helper.setSubject(subject);
        helper.setText(errorMessage);

        emailSender.send(message);
    }
}