package com.login.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.login.app.model.User;
import com.login.app.service.UserService;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //add user
    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user){
        return userService.addUser(user);
    }
    
    //get user
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value="id") Long id){
        return userService.getUser(id);
    }
    //get user<list>
    @GetMapping("/user")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(value="id") Long id){
        return userService.deleteUser(id);
    }
    //delete user
    //upadate user?
    
    //register user
    //login user

    //fazer um cadastro de peças e usar o usuario logado e a role para fazer as permissoes de acesso

}
