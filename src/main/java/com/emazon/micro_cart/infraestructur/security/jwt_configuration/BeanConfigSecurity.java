package com.emazon.micro_cart.infraestructur.security.jwt_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class BeanConfigSecurity {
    
     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    
}
