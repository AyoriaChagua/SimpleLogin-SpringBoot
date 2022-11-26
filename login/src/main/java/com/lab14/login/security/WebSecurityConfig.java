package com.lab14.login.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    //injectamos 
    private final UserDetailsService userDetailsService;
    private final JWTAuthorizationFilter authorizationFilter; // No es gestionado por Spring por ser una clase sencilla, se crea una instancia del objeto(necesirta un authmanager)
   
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authManager);
        authenticationFilter.setFilterProcessesUrl("/login");
        return http
            .csrf()
            .disable()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(authenticationFilter)
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // @Bean //Probando la implementacion
    // UserDetailsService userDetailService(){
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     manager.createUser(User.withUsername("admin")
    //         .password(passwordEncoder().encode("admin"))
    //         .roles()
    //         .build()
    //     );
    //     return manager;
    // }

    @Bean 
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder())
        .and()
        .build();


    }

    @Bean //Config for password
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    public static void main(String[] args) {
        System.out.println("PASS: " + new BCryptPasswordEncoder().encode("admin"));
    }
}
