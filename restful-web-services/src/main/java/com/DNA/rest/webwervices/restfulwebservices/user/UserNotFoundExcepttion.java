package com.DNA.rest.webwervices.restfulwebservices.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundExcepttion extends RuntimeException {
    public UserNotFoundExcepttion(String message) {
        super(message);
    }
}
