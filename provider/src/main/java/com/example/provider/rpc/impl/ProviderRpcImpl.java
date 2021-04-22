package com.example.provider.rpc.impl;

import com.example.providerapi.ProviderRpcService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ProviderRpcImpl implements ProviderRpcService {

    @Value("${server.port}")
    private String currentPort;

    @Override
    public String provide() {
        return currentPort;
    }
}
