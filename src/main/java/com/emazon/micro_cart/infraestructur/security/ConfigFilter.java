package com.emazon.micro_cart.infraestructur.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.emazon.micro_cart.infraestructur.security.jwt_configuration.JwtAutenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class ConfigFilter {
    private final JwtAutenticationFilter jwtAuthFilter;
    private final CustomEntryPoint customEntryPoint;

     @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
        .csrf(csrf -> csrf.disable()) 
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
            .anyRequest()
            .authenticated()    
             
         )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ) 
        .exceptionHandling(exceptionHandling -> 
                exceptionHandling.authenticationEntryPoint(customEntryPoint) 
            )
        .build();
    }
     
}
