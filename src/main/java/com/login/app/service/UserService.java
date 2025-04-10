package com.login.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.app.model.User;
import com.login.app.repository.UserRepository;

@Service
public class UserService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    private UserRepository userRepository;
    
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    
    
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
            JWTService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<User> addUser(User user) {
        
        Optional<User> userToCheck = userRepository.findByUsername(user.getUsername());

        if(userToCheck.isPresent() == true){
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(encoder.encode(user.getPassword()));
        User userToSave = userRepository.save(user);
        return ResponseEntity.ok(userToSave);
    }

    public ResponseEntity<User> getUser(Long id) {
        Optional<User> userToGet = userRepository.findById(id);

        if(userToGet.isPresent() == false){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userToGet.get());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> deleteUser(Long id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if(userToDelete.isPresent() == false){
            return ResponseEntity.badRequest().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public String login(User user) {
        Authentication authentication = 
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        // If the user is authenticated, generate a JWT token
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user);
        }

        return "failure";
    }

    

}
