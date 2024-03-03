package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.entity.OrderListEntity;

public interface OrderListRepository extends JpaRepository<OrderListEntity, Long> {


     OrderListEntity findByUserId(Long Id);
}
