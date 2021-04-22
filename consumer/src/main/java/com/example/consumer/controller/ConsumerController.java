package com.example.consumer.controller;

import com.example.providerapi.ProviderRpcService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConsumerController {

    @Value("${test.consume}")
    private String consumeTest;

    @Reference
    ProviderRpcService providerRpcService;

    @GetMapping
    public String consume(){
        return "consume config test via rest:"+consumeTest;
    }

    @GetMapping("/rpc_test")
    public String rpcTest(){
        return "rpc test ,result:"+providerRpcService.provide();
    }

    @GetMapping("feign_test")
    public String feignTest(){
        return "feign test ,result:";
    }

}
