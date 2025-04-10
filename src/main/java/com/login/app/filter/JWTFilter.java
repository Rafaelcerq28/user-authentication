package com.login.app.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.login.app.service.JWTService;
import com.login.app.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        // Extracts the Authorization header from the request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Checks if the Authorization header is not null and starts with "Bearer "
        // If it does, extracts the token from the header
        // Extracts the username from the token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        // If the username is not null and the user is not already authenticated
        // Authenticates the user
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            
            // Loads the user details from the database
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            
            // Validates the token
            if(jwtService.validateToken(token, userDetails)){
                // Creates an authentication token with the user details
                UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Sets the authentication token in the SecurityContext
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);    
            }
        }

        // Calls the next filter in the chain
        filterChain.doFilter(request, response);
    }

}
