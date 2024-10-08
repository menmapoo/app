package com.example.E_ecommerce.repository;

import com.example.E_ecommerce.entities.Order;
import com.example.E_ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserIdAndOrderStatus(Long orderId, OrderStatus orderStatus);
}
