package com.example.demo.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service

public class EmailService {

	   @Autowired
	    private JavaMailSender javaMailSender;

	    public void sendEmail(String recipientEmail, String subject, String body, File attachment) {
	        MimeMessage message = javaMailSender.createMimeMessage();

	        try {
	            if (StringUtils.isEmpty(recipientEmail)) {
	                throw new IllegalArgumentException("Recipient email cannot be null or empty");
	            }

	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom("santhoshk65656@gmail.com");
	            helper.setTo(recipientEmail);
	            helper.setSubject(subject);

	            if (attachment != null) {
	                helper.setText(body);
	                helper.addAttachment("invoice.pdf", attachment);
	            } else {
	                helper.setText(body, true); // set true for HTML content, if applicable
	            }

	            javaMailSender.send(message);
	        } catch (MessagingException | IllegalArgumentException e) {
	            
	            e.printStackTrace();
	        }
	    }
	}

