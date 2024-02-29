package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.entity.CartProductEntity;
import project3.nutrisubscriptionservice.entity.CartProductKey;

public interface CartProductRepository extends JpaRepository<CartProductEntity, CartProductKey> {
}