package com.example.demo.controller;

import com.example.demo.service.ConstructorInjectionService;
import com.example.demo.service.FieldInjectionService;
import com.example.demo.service.SetterInjectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiController {
    
    private final FieldInjectionService fieldInjectionService;
    private final ConstructorInjectionService constructorInjectionService;
    private final SetterInjectionService setterInjectionService;
    
    public DiController(FieldInjectionService fieldInjectionService,
                        ConstructorInjectionService constructorInjectionService,
                        SetterInjectionService setterInjectionService) {
        this.fieldInjectionService = fieldInjectionService;
        this.constructorInjectionService = constructorInjectionService;
        this.setterInjectionService = setterInjectionService;
    }
    
    @GetMapping("/field")
    public String fieldInjection() {
        fieldInjectionService.sendNotification("Test");
        return "Field Injection Done";
    }
    
    @GetMapping("/constructor")
    public String constructorInjection() {
        constructorInjectionService.sendNotification("Test");
        return "Constructor Injection Done";
    }
    
    @GetMapping("/setter")
    public String setterInjection() {
        setterInjectionService.sendNotification("Test");
        return "Setter Injection Done";
    }
}
