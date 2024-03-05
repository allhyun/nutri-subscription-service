package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.entity.CartEntity;
import project3.nutrisubscriptionservice.entity.CartProductEntity;
import project3.nutrisubscriptionservice.entity.CartProductKey;
import project3.nutrisubscriptionservice.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProductEntity, CartProductKey> {
    Optional<CartProductEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
    //List<CartProductEntity> findByUser(Long cartPid);

}
