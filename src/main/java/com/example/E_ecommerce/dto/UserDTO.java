package com.example.E_ecommerce.dto;


import com.example.E_ecommerce.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;


}
