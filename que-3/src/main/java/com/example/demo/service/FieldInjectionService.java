package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldInjectionService {
    
    @Autowired
    private EmailService emailService;
    
    public void sendNotification(String msg) {
        emailService.sendEmail(msg);
    }
}
