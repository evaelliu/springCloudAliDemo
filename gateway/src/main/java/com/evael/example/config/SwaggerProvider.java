package com.evael.example.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v3/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;

    private static final String DISCOVERY_CLIENT_PREFIX = "ReactiveCompositeDiscoveryClient_";
    private static final String DISCOVERY_RPC_PROVIDER_PREFIX = "providers:";
    private static final String DISCOVERY_RPC_CONSUMER_PREFIX = "consumers:";

    @Value("#{'${xt.defined.services:}'.split(',')}")
    List<String> definedServices;

    @Override
    public List<SwaggerResource> get() {
        Set<SwaggerResource> resources = new HashSet<>();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
                                predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                        .replace("/**", API_URI)))));
        if(!CollectionUtils.isEmpty(definedServices))
            definedServices.forEach(service -> {
                if(!StringUtils.isEmpty(service))
                    resources.add(swaggerResource(service,'/'+service+API_URI));
            });
        routes.forEach(route -> {
            if(route.startsWith(DISCOVERY_CLIENT_PREFIX)) {
                String service = route.substring(DISCOVERY_CLIENT_PREFIX.length());
                if(!StringUtils.isEmpty(service)  &&
                        !service.startsWith(DISCOVERY_RPC_PROVIDER_PREFIX) &&
                        !service.startsWith(DISCOVERY_RPC_CONSUMER_PREFIX))
                    resources.add(swaggerResource(service,'/'+service+API_URI));
            }
        });
        return new ArrayList<>(resources);
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("3.0");
        return swaggerResource;
    }
}