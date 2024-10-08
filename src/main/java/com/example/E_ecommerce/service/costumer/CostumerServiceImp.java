package com.example.E_ecommerce.service.costumer;

import com.example.E_ecommerce.dto.CartItemsDTO;
import com.example.E_ecommerce.dto.OrderDTO;
import com.example.E_ecommerce.dto.PlaceOrderDto;
import com.example.E_ecommerce.dto.ProductDTO;
import com.example.E_ecommerce.entities.CartItems;
import com.example.E_ecommerce.entities.Order;
import com.example.E_ecommerce.entities.Product;
import com.example.E_ecommerce.entities.User;
import com.example.E_ecommerce.enums.OrderStatus;
import com.example.E_ecommerce.repository.CartItemRepository;
import com.example.E_ecommerce.repository.OrderRepository;
import com.example.E_ecommerce.repository.ProductRepository;
import com.example.E_ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostumerServiceImp implements CostumerService {
    private static final Logger logger = LoggerFactory.getLogger(CostumerServiceImp.class);

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(Product::getProductDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProductByTitle(String title) {
        return productRepository.findByNameContaining(title).stream().map(Product::getProductDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> addProductToCart(CartItemsDTO cartItemsDTO) {
        Order pendingOrder = orderRepository.findByUserIdAndOrderStatus(cartItemsDTO.getUserId(),OrderStatus.PENDING);
        if (pendingOrder == null) {
            pendingOrder = new Order();
            pendingOrder.setOrderStatus(OrderStatus.PENDING);
            User user = userRepository.findById(cartItemsDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            pendingOrder.setUser(user);
            pendingOrder.setPrice("0"); // Initialize the order price to zero
            pendingOrder = orderRepository.save(pendingOrder);
            logger.debug("Created new pending order for userId={}", cartItemsDTO.getUserId());
        }

        Optional<CartItems> optionalCartItems = cartItemRepository.findByUserIdAndProductIdAndOrderId(
                cartItemsDTO.getUserId(),
                cartItemsDTO.getProductId(),
                pendingOrder.getId()
        );

        if (optionalCartItems.isPresent()){
            CartItemsDTO productAlreadyExistInCart = new CartItemsDTO();
            productAlreadyExistInCart.setProductId(null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(productAlreadyExistInCart);
        }else {
            Optional<Product> optionalProduct = productRepository.findById(cartItemsDTO.getProductId());
            Optional<User> optionalUser = userRepository.findById(cartItemsDTO.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()){
                Product product = optionalProduct.get();
                CartItems cartItems = new CartItems();
                cartItems.setProduct(product);
                cartItems.setUser(optionalUser.get());
                cartItems.setQuantity(String.valueOf(1L));
                cartItems.setOrder(pendingOrder);
                cartItems.setPrice(product.getPrice());
                CartItems updateCart = cartItemRepository.save(cartItems);
                Long newPrice = Long.parseLong(pendingOrder.getPrice()) + Long.parseLong(cartItems.getPrice());
                pendingOrder.setPrice(String.valueOf(newPrice));
                pendingOrder.getCartItems().add(cartItems);
                orderRepository.save(pendingOrder);
                CartItems updateCartItemDto = new CartItems();
                updateCartItemDto.setId(updateCart.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(updateCartItemDto);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }
        }
    }

    @Override
    public OrderDTO getCartByUserId(Long userId) {

        Order pendingOrder = orderRepository.findByUserIdAndOrderStatus(userId,OrderStatus.PENDING);
        if (pendingOrder == null){
            return null;
        }
        List<CartItemsDTO> cartItemList = pendingOrder.getCartItems().stream().map(CartItems::getcartItemsDTO).toList();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCartItemsDtoList(cartItemList);
        orderDTO.setAmount(pendingOrder.getPrice());
        orderDTO.setId(pendingOrder.getId());
        orderDTO.setOrderStatus(pendingOrder.getOrderStatus());
        return orderDTO;


    }

    @Override
    public OrderDTO addMinusOnProduct(Long userId, Long productId) {
        Order pendingOrder = orderRepository.findByUserIdAndOrderStatus(userId,OrderStatus.PENDING);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<CartItems> optionalCartItems = cartItemRepository.findByUserIdAndProductIdAndOrderId(userId,productId,pendingOrder.getId());

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            cartItems.setQuantity(String.valueOf( Integer.parseInt(optionalCartItems.get().getQuantity()) - 1));
            pendingOrder.setPrice(String.valueOf(Long.parseLong(pendingOrder.getPrice()) - Long.parseLong(optionalProduct.get().getPrice())));
            cartItemRepository.save(cartItems);
            orderRepository.save(pendingOrder);
            return pendingOrder.getOrderDto();
        }
        return null;
    }

    @Override
    public OrderDTO addPlusOnProduct(Long userId, Long productId) {
        Order pendingOrder = orderRepository.findByUserIdAndOrderStatus(userId,OrderStatus.PENDING);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<CartItems> optionalCartItems = cartItemRepository.findByUserIdAndProductIdAndOrderId(userId,productId,pendingOrder.getId());

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()){
            CartItems cartItems = optionalCartItems.get();
            cartItems.setQuantity(String.valueOf( Integer.parseInt(optionalCartItems.get().getQuantity()) + 1));
            pendingOrder.setPrice(String.valueOf(Long.parseLong(pendingOrder.getPrice()) + Long.parseLong(optionalProduct.get().getPrice())));
            cartItemRepository.save(cartItems);
            orderRepository.save(pendingOrder);
            return pendingOrder.getOrderDto();
        }
        return null;
    }

    @Override
    public OrderDTO placeOrder(PlaceOrderDto placeOrderDto) {
        Order existingOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(),OrderStatus.PENDING);
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
        if (optionalUser.isPresent()){
            existingOrder.setOrderStatus(OrderStatus.SUBMITTED);
            existingOrder.setAddress(placeOrderDto.getAddress());
            existingOrder.setDate(new Date());
            existingOrder.setPaymentType(placeOrderDto.getPayment());
            existingOrder.setDescription(placeOrderDto.getOrderDescription());
            existingOrder.setPrice(existingOrder.getPrice());
            orderRepository.save(existingOrder);

            Order newOrder  = new Order();
            newOrder .setOrderStatus(OrderStatus.PENDING);
            newOrder .setUser(optionalUser.get());
            newOrder .setPrice("0");
            return newOrder.getOrderDto();
        }
        return null;
    }


}
