package com.example.E_ecommerce.service.user;

import com.example.E_ecommerce.dto.SignupDTO;
import com.example.E_ecommerce.dto.UserDTO;

public interface UserService {
    UserDTO createUser(SignupDTO signupDTO);

    boolean hasUserwithEmail(String email);
}
