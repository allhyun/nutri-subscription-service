package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.entity.OrderEntity;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity , Long> {
    List<OrderEntity> findByUserId(Long id);

}
