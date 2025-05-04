package com.emazon.micro_cart.infraestructur.security.jwt_configuration;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.emazon.micro_cart.infraestructur.util.ConstantsInfraestructure;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAutenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final Validator validator;
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(ConstantsInfraestructure.AUTHORIZATION);
        final String jwt;
        final String userName;
        
        Set<ConstraintViolation<Object>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if (authHeader == null || !authHeader.startsWith(ConstantsInfraestructure.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        userName = jwtService.extractUsername(jwt);
         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                 userName, 
                 null,
                jwtService.getAuthorities(jwt) 
                );
        SecurityContextHolder.getContext().setAuthentication(authToken);
            
        
        filterChain.doFilter(request, response);
      }

    }

