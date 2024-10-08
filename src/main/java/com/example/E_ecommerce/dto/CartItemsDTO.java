package com.example.E_ecommerce.dto;

import lombok.Data;

@Data
public class CartItemsDTO {

    private Long id;

    private String price;

    private String quantity;

    private Long productId;

    private Long orderId;

    private String productName;

    private byte[] returnedImg;

    private Long userId;
}
