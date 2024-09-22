package com.emazon.micro_cart.infraestructur.security.jwt_configuration;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.emazon.micro_cart.infraestructur.util.ConstantsInfraestructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
    
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(ConstantsInfraestructure.KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
         return extractAllClaims(token).getSubject();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);  
    }


    private Claims extractAllClaims(String token) throws SignatureException {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            e.printStackTrace();
            return Jwts.claims();
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        Object roleClaim = claims.get("role");
    
        if (roleClaim == null) {
            return Collections.emptyList();
        }
    
        if (roleClaim instanceof String) {
            String role = (String) roleClaim;
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            return List.of(new SimpleGrantedAuthority(role));
        } 
    
        return Collections.emptyList();
    }
    
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class)); 
    }
}