package project3.nutrisubscriptionservice.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.entity.*;
import project3.nutrisubscriptionservice.repository.CartProductRepository;
import project3.nutrisubscriptionservice.repository.CartRepository;
import project3.nutrisubscriptionservice.repository.ProductRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
@Component
@Slf4j
@AllArgsConstructor
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private FormService formService;

    public Optional<CartEntity> getCartByUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Optional.empty();
        }
        return cartRepository.findByUser(user);
    }

    // 상품을 장바구니에 추가하는 메소드
    public void addProductToCart(Long userId, Long productId, int quantity) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
        }

        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder().user(user).build();
                    cartRepository.save(newCart);
                    return newCart;
                });

        CartProductKey cartProductKey = CartProductKey.builder()
                .cartId(cart.getCartId())
                .productId(product.getProductId())
                .build();

        CartProductEntity cartProduct = CartProductEntity.builder()
                .cartProductId(cartProductKey)
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();

        cartProductRepository.save(cartProduct);
    }

    // 상품을 장바구니에서 제거하는 메소드
    public void removeProductFromCart(Long userId, Long productId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
        }

        CartEntity cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            throw new NoSuchElementException("장바구니가 비어있습니다.");
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
        }

        CartProductEntity cartProduct = cartProductRepository.findByCartAndProduct(cart, product).orElse(null);
        if (cartProduct == null) {
            throw new NoSuchElementException("장바구니에 해당 상품이 없습니다.");
        }

        cartProductRepository.delete(cartProduct);
    }


    public List<CartProductEntity> addRecommendedProducts(Long userId, Long resultId) {
        // 설문조사 결과에 따른 추천 제품들을 가져옵니다.
        List<ProductDTO> recommendedProductsDTO = formService.recommendProducts(resultId);

        // ProductDTO를 ProductEntity로 변환합니다.
        List<ProductEntity> recommendedProducts = recommendedProductsDTO.stream()
                .map(dto -> productRepository.findById(dto.getProduct_id()).orElseThrow(
                        () -> new NoSuchElementException("해당 상품을 찾을 수 없습니다.")
                )).toList();

        // 사용자의 장바구니를 가져옵니다.
        CartEntity userCart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다.")
        )).orElseGet(() -> {
            CartEntity newCart = CartEntity.builder().user(userRepository.findById(userId).orElseThrow(
                    () -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다.")
            )).build();
            cartRepository.saveAndFlush(newCart);  // saveAndFlush 메소드를 사용합니다.
            return newCart;
        });

        List<CartProductEntity> cartProducts = new ArrayList<>();

        // 추천 제품들을 사용자의 장바구니에 추가합니다.
        for (ProductEntity product : recommendedProducts) {
            // CartProductKey를 생성합니다.
            CartProductKey cartProductKey = new CartProductKey();
            cartProductKey.setCartId(userCart.getCartId());
            cartProductKey.setProductId(product.getProductId());

            // CartProductEntity를 생성합니다.
            CartProductEntity cartProduct = CartProductEntity.builder()
                    .cartProductId(cartProductKey) // 여기에 cartProductId를 설정합니다.
                    .cart(userCart)
                    .product(product)
                    .quantity(1) // 장바구니에 추가할 때의 기본 수량을 1로 설정했습니다.
                    .build();
            cartProductRepository.save(cartProduct);
            cartProducts.add(cartProduct);
        }

        return cartProducts;
    }
}