package com.example.provider.rpc.impl;

import com.example.providerapi.ProviderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService(interfaceClass = ProviderRpcService.class)
public class ProviderRpcImpl implements ProviderRpcService {

    @Value("${server.port}")
    private String currentPort;

    @Override
    public String provide() {
        return "rpc:"+currentPort;
    }
}
