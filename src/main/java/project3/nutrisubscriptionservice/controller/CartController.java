package project3.nutrisubscriptionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.CartDTO;
import project3.nutrisubscriptionservice.dto.CartProductDTO;
import project3.nutrisubscriptionservice.entity.CartEntity;
import project3.nutrisubscriptionservice.entity.CartProductEntity;
import project3.nutrisubscriptionservice.service.CartService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable Long userId) {
        Optional<CartEntity> cart = cartService.getCartByUser(userId);
        if (cart.isPresent()) {
            CartDTO cartDTO = CartDTO.builder()
                    .cartId(cart.get().getCartId())
                    .userId(cart.get().getUser().getId())
                    .cartProducts(cart.get().getCartProducts().stream()
                            .map(cartProduct -> CartProductDTO.builder()
                                    .productId(cartProduct.getProduct().getProductId())
                                    .quantity(cartProduct.getQuantity())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}/add-recommended-products")
    public ResponseEntity<List<CartProductEntity>> addRecommendedProducts(@PathVariable Long userId, @RequestParam Long resultId) {
        List<CartProductEntity> cartProducts = cartService.addRecommendedProducts(userId, resultId);
        return new ResponseEntity<>(cartProducts, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}/add")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @RequestParam Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



