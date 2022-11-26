package com.lab14.login.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenUtils {
    //La mejor forma de implmentar esto es usarlo en los tokens
    private final static String ACCESS_TOKEN_SECRET = "j12jbbjwabfjas34hztDkUs";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;

    //Produciendo token que será enviado al cliente
    public static String createToken(String name, String email){
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() * expirationTime);
        Map<String, Object> extra = new HashMap<>();
        extra.put("name", name);
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(expirationDate)
            .addClaims(extra)
            //https://github.com/jhipster/generator-jhipster/issues/8165
            .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
            .compact();
    }
    //retornando un token para spring boot security y pasar por el proceso de autorizacion en formato plano
    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }catch(JwtException e){
            return null;
        }

    }
}
