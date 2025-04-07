package com.login.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.login.app.model.User;
import com.login.app.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> addUser(User user) {
        
        Optional<User> userToCheck = userRepository.findByUsername(user.getUsername());

        if(userToCheck.isPresent() == true){
            return ResponseEntity.badRequest().build();
        }

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

    

}
