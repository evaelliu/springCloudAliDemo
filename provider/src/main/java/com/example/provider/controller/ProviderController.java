package com.example.provider.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ProviderController {

    @Value("${server.port}")
    private String currentPort;

    @GetMapping
    public String provide(){
        return "provider running port:"+currentPort;
    }
}
