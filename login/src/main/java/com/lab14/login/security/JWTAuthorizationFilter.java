package com.lab14.login.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//Filtro para el proceso de autorizacion, para utilizar el token obtenido adjuntado a las solicitudes
@Component//La notacion hace que sea gestionado como un objeto por el nucleo Spring, para usar la ventjas ofrecidas
public class JWTAuthorizationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.replace("Bearer ",  "");
            UsernamePasswordAuthenticationToken usernamePat = TokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePat);
        }
        filterChain.doFilter(request, response);
    }
}
