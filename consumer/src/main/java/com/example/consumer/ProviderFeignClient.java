package com.example.consumer;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface ProviderFeignClient {

    @GetMapping
    String provide();
}
