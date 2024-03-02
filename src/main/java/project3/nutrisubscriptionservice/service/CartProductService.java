package project3.nutrisubscriptionservice.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.entity.CartProductEntity;
import project3.nutrisubscriptionservice.entity.CartProductKey;
import project3.nutrisubscriptionservice.repository.CartProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Component
@Slf4j
@AllArgsConstructor
public class CartProductService {

    @Autowired
    CartProductRepository cartProductRepository;

    public CartProductEntity save(CartProductEntity cartProduct) {
        return cartProductRepository.save(cartProduct);
    }

    public Optional<CartProductEntity> findById(Long cartId, Long productId) {
        CartProductKey cartProductKey = new CartProductKey(cartId, productId);
        return cartProductRepository.findById(cartProductKey);
    }

    public List<CartProductEntity> findAll() {
        return cartProductRepository.findAll();
    }

    public void deleteById(Long cartId, Long productId) {
        CartProductKey cartProductKey = new CartProductKey(cartId, productId);
        cartProductRepository.deleteById(cartProductKey);
    }
}
