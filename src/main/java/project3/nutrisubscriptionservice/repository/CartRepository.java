package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
