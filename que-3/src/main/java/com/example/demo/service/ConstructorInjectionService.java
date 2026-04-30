package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ConstructorInjectionService {
    
    private final EmailService emailService;
    
    public ConstructorInjectionService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    public void sendNotification(String msg) {
        emailService.sendEmail(msg);
    }
}
//////