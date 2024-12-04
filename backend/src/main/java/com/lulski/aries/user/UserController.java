package com.lulski.aries.user;

import java.net.http.HttpRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class UserController {

    /**
     * insert user
     * @param request
     * @return
     */
    @PostMapping("/api/v1/user")
    public ResponseEntity<String> addUser(HttpRequest request) {

        System.out.println(">>>");

        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }
}
