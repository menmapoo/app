package com.example.E_ecommerce.entities;

import com.example.E_ecommerce.dto.UserDTO;
import com.example.E_ecommerce.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private byte[] img;

}
