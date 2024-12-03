package com.lulski.aries.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;


@RestController
public class UserController {

    @PostMapping("/api/v1/user")
    public ResponseEntity<String> addUser(HttpRequest request){

        System.out.println(">>>");

        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }
}
