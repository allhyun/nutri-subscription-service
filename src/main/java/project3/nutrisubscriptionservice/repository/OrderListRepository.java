package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface OrderListRepository extends JpaRepository<OrderListEntity, Long> {


    List< OrderListEntity> findByUser(UserEntity user);

    Optional<OrderListEntity> findById(Long id);
    OrderListEntity findByUserId(Long id);
}
