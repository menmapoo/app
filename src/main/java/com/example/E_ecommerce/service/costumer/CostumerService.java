package com.example.E_ecommerce.service.costumer;

import com.example.E_ecommerce.dto.CartItemsDTO;
import com.example.E_ecommerce.dto.OrderDTO;
import com.example.E_ecommerce.dto.PlaceOrderDto;
import com.example.E_ecommerce.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CostumerService {

    List<ProductDTO> getAllProducts();
    List<ProductDTO> searchProductByTitle(String title);
    ResponseEntity<?> addProductToCart(CartItemsDTO cartItemsDTO);
    OrderDTO getCartByUserId(Long userId);
    OrderDTO addMinusOnProduct(Long userId,Long productId);
    OrderDTO addPlusOnProduct(Long userId,Long productId);
    OrderDTO placeOrder(PlaceOrderDto placeOrderDto);


}
