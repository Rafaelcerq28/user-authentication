package com.login.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean // Marks this method as a Spring bean, meaning Spring will manage its lifecycle
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    
        // Disables CSRF protection. Useful for stateless APIs but should be used with caution.
        http.csrf(customizer -> customizer.disable()); 
        
        // Configura as regras de autorização
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/register","/login").permitAll() // Permite acesso ao endpoint /register sem autenticação
            .anyRequest().authenticated() // Requer autenticação para qualquer outro endpoint
        );
        
        // Enables HTTP Basic authentication, allowing clients to authenticate via an Authorization header.
        http.httpBasic(Customizer.withDefaults());
        
        // Configures session management to be stateless. 
        // This is ideal for APIs where each request should be authenticated independently.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds and returns the configured SecurityFilterChain.
        return http.build();
    }
}
