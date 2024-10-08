package com.example.E_ecommerce.entities;

import com.example.E_ecommerce.dto.CartItemsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id", "order_id"})
})
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String price;
    private String quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public CartItemsDTO getcartItemsDTO(){

        CartItemsDTO cartItemsDTO = new CartItemsDTO();

        cartItemsDTO.setId(id);
        cartItemsDTO.setQuantity(quantity);
        cartItemsDTO.setPrice(price);
        cartItemsDTO.setProductId(product != null?product.getId():null);
        cartItemsDTO.setProductName(product != null?product.getName():null);
        cartItemsDTO.setReturnedImg(product != null?product.getImage():null);
        cartItemsDTO.setUserId(user != null?user.getId():null);


        return cartItemsDTO;
    }


}
