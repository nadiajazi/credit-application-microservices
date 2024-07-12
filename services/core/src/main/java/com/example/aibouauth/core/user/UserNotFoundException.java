package com.example.aibouauth.core.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer id){
        super("There is no user with the id :"+id);
    }
}
