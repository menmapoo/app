package com.example.E_ecommerce.service.user;

import com.example.E_ecommerce.dto.SignupDTO;
import com.example.E_ecommerce.dto.UserDTO;
import com.example.E_ecommerce.entities.Order;
import com.example.E_ecommerce.entities.User;
import com.example.E_ecommerce.enums.OrderStatus;
import com.example.E_ecommerce.enums.UserRole;
import com.example.E_ecommerce.repository.OrderRepository;
import com.example.E_ecommerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;


    @PostConstruct
    public void createAdminAcount(){
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
        if (adminAccount == null){
            User user = new User();
            user.setUserRole(UserRole.ADMIN);
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);

        }
    }


    @Override
    public UserDTO createUser(SignupDTO signupDTO) {

        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setUserRole(UserRole.USER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        User createdUser = userRepository.save(user);
        Order order = new Order();
        order.setPrice(String.valueOf(0));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(createdUser);
        orderRepository.save(order);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getId());
        userDTO.setName(createdUser.getName());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setUserRole(createdUser.getUserRole());
        return userDTO;
    }

    @Override
    public boolean hasUserwithEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }
}
