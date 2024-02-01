package com.dna.springboot.myfirstwepapp.login;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public boolean authenticate(String username, String password) {
        boolean isValidUser = username.equalsIgnoreCase("DNA");
        boolean isValidPassword = password.equalsIgnoreCase("dummy");
        
        return isValidUser && isValidPassword;
    }
}
