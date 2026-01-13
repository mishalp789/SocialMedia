package com.mishal.socialmedia.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getWelcomeMessage(){
        return "Welcome to the Social Media App";
    }
}
