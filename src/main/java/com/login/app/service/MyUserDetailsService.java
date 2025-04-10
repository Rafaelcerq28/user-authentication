package com.login.app.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.app.model.User;
import com.login.app.model.UserAuthenticated;
import com.login.app.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //This method is used to load user-specific data
    //It is used by Spring Security to load user-specific data
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        //If user is not found, throw an exception
        if(user.isPresent() ==false){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        //Return the UserPrincipal object (which implements UserDetails interface)
        return new UserAuthenticated(user.get());
    }

}
