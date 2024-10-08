package com.example.E_ecommerce.controller;


import com.example.E_ecommerce.dto.SignupDTO;
import com.example.E_ecommerce.dto.UserDTO;
import com.example.E_ecommerce.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @Autowired
    private UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO){
        if (userService.hasUserwithEmail(signupDTO.getEmail())){
            return new ResponseEntity<>("User already exist",HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO createdUser = userService.createUser(signupDTO);
        if (createdUser == null){
            return new ResponseEntity<>("User not created come again later", HttpStatus.BAD_REQUEST);
        }return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
    }
}
