/*
 * Copyright (c) 2019 Jalasoft.
 *
 * This software is the confidential and proprietary information of Jalasoft.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jalasoft.
 */

package com.jalasoft.jfc;

import com.google.common.collect.Lists;

import com.jalasoft.jfc.model.utility.PathJfc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Run Sprint Boot to Web Application
 *
 * @version 0.1 13 Dic 2019
 *
 * @author Enrique Carrizales
 */
@Configuration
@SpringBootApplication
@EnableSwagger2
@ServletComponentScan
public class Main {

    /**
     * Main method executes SprintApplication
     * @param args receives String array
     */
    public static void main(String[] args) {
        System.out.println("Welcome to JFC");
        PathJfc pathJfc = new PathJfc();
        SpringApplication.run(Main.class, args);
    }

    /**
     * Allows to configure swagger.
     * @return swagger configuration.
     */
    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.jalasoft.jfc.controller")).build().apiInfo(apiDetails()).
                securitySchemes(Arrays.asList(apiKey())).securityContexts(Lists.newArrayList(securityContext()));
    }

    /**
     * Generates details of ApiInfo.
     * @return ApiInfo description.
     */
    ApiInfo apiDetails() {
        return new ApiInfo("JFC Converter API", "Prog-102", "0.1",
                "Free to use", new Contact("AT-11", "http://fundacion-jala.org",
                "at-11@fundacion-jala.org"), "API license", "http://fundacion-jala.org",
                Collections.emptyList());
    }

    /**
     * Allows to specify security context.
     * @return context value.
     */
    @Bean
    SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * Allows to define default value.
     * @return list of security reference.
     */
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("TJW", authorizationScopes));
    }

    /**
     * Defines apiKey value.
     * @return api key value.
     */
    ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
}
