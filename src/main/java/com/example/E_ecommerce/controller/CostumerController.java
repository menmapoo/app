package com.example.E_ecommerce.controller;

import com.example.E_ecommerce.dto.CartItemsDTO;
import com.example.E_ecommerce.dto.OrderDTO;
import com.example.E_ecommerce.dto.PlaceOrderDto;
import com.example.E_ecommerce.dto.ProductDTO;
import com.example.E_ecommerce.entities.Order;
import com.example.E_ecommerce.service.costumer.CostumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/costumer")
@CrossOrigin(origins = "http://localhost:4200")
public class CostumerController {


    private final CostumerService costumerService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProduct(){
        List<ProductDTO> productList = costumerService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/products/search/{title}")
    public ResponseEntity<List<ProductDTO>> searchProductByTitle(@PathVariable String title){
        List<ProductDTO> productList = costumerService.searchProductByTitle(title);
        return ResponseEntity.ok(productList);
    }

    @PostMapping("/carts")

    public ResponseEntity<?> postProductCart(@RequestBody CartItemsDTO cartItemsDTO){
        return costumerService.addProductToCart(cartItemsDTO);
    }



    @GetMapping("/cart/{userId}")
    public ResponseEntity<OrderDTO> getCartByUserId(@PathVariable Long userId){

        OrderDTO orderDTO = costumerService.getCartByUserId(userId);
        if (orderDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDTO);

    }

    @GetMapping("/{userId}/deduct/{productId}")
    public ResponseEntity<OrderDTO> addMinusOnProduct(@PathVariable Long userId,@PathVariable Long productId){
        OrderDTO orderDTO = costumerService.addMinusOnProduct(userId,productId);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/{userId}/add/{productId}")
    public ResponseEntity<OrderDTO> addPlusOnProduct(@PathVariable Long userId,@PathVariable Long productId){
        OrderDTO orderDTO = costumerService.addPlusOnProduct(userId,productId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/orderPlace")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
        OrderDTO orderDTO = costumerService.placeOrder(placeOrderDto);
        if (orderDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }


}

