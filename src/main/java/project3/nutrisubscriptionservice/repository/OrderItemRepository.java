package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.dto.OrderItemDTO;
import project3.nutrisubscriptionservice.entity.OrderItemEntity;
import project3.nutrisubscriptionservice.entity.OrderListEntity;

import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity , Long> {
    List<OrderItemEntity> findByOrderList(OrderListEntity orderList);
    OrderItemEntity findByOrderitemId(Long orderitemId);
}
