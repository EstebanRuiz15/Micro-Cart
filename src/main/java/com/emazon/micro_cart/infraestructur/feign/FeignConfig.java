package com.emazon.micro_cart.infraestructur.feign;

import java.util.Objects;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.Client;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new ApacheHttpClient(httpClient);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            HttpServletRequest request = ((ServletRequestAttributes) Objects
                    .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String jwt = request.getHeader("Authorization");
            template.header("Authorization", jwt);
        };
    }

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }

}