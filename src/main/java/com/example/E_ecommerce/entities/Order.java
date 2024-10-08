package com.example.E_ecommerce.entities;


import com.example.E_ecommerce.dto.OrderDTO;
import com.example.E_ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String address;

    private String paymentType;

    private Date date;

    private String price;

    private OrderStatus orderStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "order")
    private List<CartItems> cartItems = new ArrayList<>();

    public OrderDTO getOrderDto() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        return orderDTO;
    }
}
