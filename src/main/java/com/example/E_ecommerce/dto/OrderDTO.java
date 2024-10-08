package com.example.E_ecommerce.dto;


import com.example.E_ecommerce.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {

    private String orderDescription;
    private List<CartItemsDTO> cartItemsDtoList;
    private Long id;
    private Date date;
    private String amount;
    private String address;
    private OrderStatus orderStatus;
    private String paymentType;
    private String username;






}
