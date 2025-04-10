package com.login.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.login.app.filter.JWTFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JWTFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JWTFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
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
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds and returns the configured SecurityFilterChain.
        return http.build();
    }

    //AuthenticationProvider is an interface that provides authentication logic
    //DaoAuthenticationProvider is an implementation of AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {

        // Creates a new DaoAuthenticationProvider, which is an implementation of AuthenticationProvider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Sets the custom UserDetailsService to be used by the DaoAuthenticationProvider
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        // Sets the custom UserDetailsService to be used by the DaoAuthenticationProvider
        // But this time, we are using a BCryptPasswordEncoder to encrypt passwords
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
