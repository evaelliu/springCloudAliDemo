/*
package com.evael.example.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

@Component
@Slf4j
@ConditionalOnExpression("${vpn.authcheck.enable:true}")
@RefreshScope
public class AuthorizeGatewayFilter implements GlobalFilter {


    @Value("#{'${ignoreurl.start:/oauth}'.split(',')}")
    List<String> ignoreUrls;

    @Value("#{'${ignore.ips:192.168.11.87}'.split(',')}")
    List<String> ignoreIps;

    @Autowired
    TokenStore tokenStore;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        boolean ignore = !path.contains("/api/");
        if (!CollectionUtils.isEmpty(ignoreUrls))
            ignore = ignore || this.ignoreUrls.stream().anyMatch(ignoreUrl -> path.startsWith(org.apache.commons.lang.StringUtils.trim(ignoreUrl)));
        if (!CollectionUtils.isEmpty(ignoreIps))
            ignore = ignore || ignoreIps.contains(getIpAddress(request));
        //    return chain.filter(exchange);
        String token = getTokenString(request);

        ServerHttpResponse response = exchange.getResponse();
        if (!ignore && StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Flux.just("{\"success\":true,\"errorCode\":401,\"message\":\"token is missing\",\"data\":null}").map(bx -> response.bufferFactory().wrap(bx.getBytes())));
            //return response.setComplete();
        }

        OAuth2AccessToken oAuth2AccessToken = null;
        if (!StringUtils.isEmpty(token)) {
            try {
                oAuth2AccessToken = tokenStore.readAccessToken(token);
            }catch (InvalidTokenException invalidTokenException){
                log.error(invalidTokenException.getLocalizedMessage(),invalidTokenException);
            }
            if (!ignore && (oAuth2AccessToken == null || oAuth2AccessToken.isExpired())) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Flux.just("{\"success\":true,\"errorCode\":401,\"message\":\"invalid token\",\"data\":null}").map(bx -> response.bufferFactory().wrap(bx.getBytes())));
                //return response.setComplete();
            }
            String payload = token.split("\\.")[1];
            //eg :  {"aud":["api-resource","file-resource","app-resource","post-resource","user-resource"],"user_name":"{\"avatarUrl\":\"/images/avatar.png\",\"id\":1173755242219520,\"nickName\":\"小桐\",\"registerName\":\"tong\",\"status\":\"AVAILABLE\",\"tenantCode\":\"001\",\"userId\":1173755242219520}","scope":["SCOPE_ALL"],"exp":1605062242,"authorities":["ROLE_ADMIN"],"jti":"e1e175b1-6888-4d40-886b-2f69bcbc361f","client_id":"xietong110_web"}
            JSONObject jsonObject = JSONObject.parseObject(new String(Base64.getDecoder().decode(payload)));
            assert jsonObject!=null;
            String user_name = jsonObject.getString("user_name");
            request.mutate().header("username",user_name);
            // TODO auth check, userId set, params convert and so on
        }
        return chain.filter(exchange);
    }

    private String getTokenString(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Authorization");
        if(token==null)
            token=request.getQueryParams().getFirst(OAuth2AccessToken.ACCESS_TOKEN);
        if(token != null && token.startsWith(OAuth2AccessToken.BEARER_TYPE))
            token = token.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        return token;
    }

    private  String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        log.info("request headers:"+headers.toString());
        String ip = headers.getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddress()==null?null:request.getRemoteAddress().getAddress().getHostAddress();
        }

        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        log.info("======= request ip : " + ip);
        return ip;
    }
}
*/
