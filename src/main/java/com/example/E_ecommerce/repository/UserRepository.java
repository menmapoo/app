package com.example.E_ecommerce.repository;

import com.example.E_ecommerce.entities.User;
import com.example.E_ecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
}
