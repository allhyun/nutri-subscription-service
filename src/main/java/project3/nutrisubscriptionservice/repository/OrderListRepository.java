package project3.nutrisubscriptionservice.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;


import java.util.List;
import java.util.Optional;
@Repository
public interface OrderListRepository extends JpaRepository<OrderListEntity, Long> {


    List< OrderListEntity> findByUser(UserEntity user);

    Optional<OrderListEntity> findById(Long id);
    List<OrderListEntity> findByUserId(Long id);


    Page<OrderListEntity> findAllByUserId(Long id, Pageable pageable);
}
