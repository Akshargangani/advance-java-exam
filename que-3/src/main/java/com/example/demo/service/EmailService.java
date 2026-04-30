package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    public void sendEmail(String msg) {
        System.out.println("Email sent: " + msg);
    }
}
