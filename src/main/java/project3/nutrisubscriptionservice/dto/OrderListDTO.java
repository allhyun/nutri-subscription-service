package project3.nutrisubscriptionservice.dto;
import lombok.*;
import project3.nutrisubscriptionservice.entity.OrderListEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
//@NoArgsConstructor
public class OrderListDTO {
    private long orderlistId;
    private long id;
    private LocalDateTime orderdate;
    private List<OrderItemDTO> orderItems;
    private List<UserDTO> user;
//    public  OrderListDTO(OrderListEntity orderListEntity) {
//        this.orderlistId=orderListEntity.getOrderlistId();
//        this.id=orderListEntity.getUser().getId();
//        this.orderdate = orderListEntity.getOrderdate();
//        orderItems=orderListEntity.getOrderItemEntitiy().stream()
//                .map(orderItemEntity -> new OrderItemDTO(orderItemEntity))
//                .collect(Collectors.toList());
//    }

    public OrderListDTO (OrderListEntity orderListEntity) {
        this.orderlistId = orderListEntity.getOrderlistId();
        this.id = orderListEntity.getUser().getId();
        this.orderdate = orderListEntity.getOrderdate();
        orderItems = orderListEntity.getOrderItemEntitiy().stream()
                .map(orderItemEntity -> new OrderItemDTO(orderItemEntity))
                .collect(Collectors.toList());
    }

    public OrderListDTO() {

    }
}
