package com.lab14.login.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Filtros para web security config
//Proceso de authenticacion
//Implementacion del intento de autenticacion
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    
    //Funcion de intento de autenticacion
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                                HttpServletResponse response) throws AuthenticationException{
        
        AuthCredentials authCredentials = new AuthCredentials();

        try {
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class); //Si se envia correctamente las credenciales
        } catch (IOException e) {// NO se hace nada por que authcredentials ya se a inicializado anteriormente
        }

        UsernamePasswordAuthenticationToken usernamePat = new UsernamePasswordAuthenticationToken(authCredentials.getEmail(), 
        authCredentials.getPassword(),
        Collections.emptyList());

        return getAuthenticationManager().authenticate(usernamePat);
    }

    //Metodo complementario para la fase de authentication.. si en caso se a autenticado correctamente 
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        
        //Se obtiene un user details                                       
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();

        //Creasmos token a partir de user details impl
        String token = TokenUtils.createToken(userDetailsImpl.getName(), userDetailsImpl.getUsername());

        //modificar la respuesta para adjuntar el nuevo token

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

}
