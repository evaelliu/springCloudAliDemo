package com.example.provider.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author jiyou
 * @Date  2016/3/25.
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

        @Value("${spring.application.name:}")
        private String applicationName;

        @Bean
        public Docket apiDocket() {
                return new Docket(DocumentationType.OAS_30)
                        .apiInfo(apiInfo())
                        .select()
                                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                                .paths(PathSelectors.any())
                                .build().pathMapping(StringUtils.isEmpty(applicationName)?"":"/" + applicationName);
        }

        private ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                        .title(applicationName)
                        //.description("Account management service")
                        .build();
        }
}
