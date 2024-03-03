package project3.nutrisubscriptionservice.dto;

import lombok.*;
import project3.nutrisubscriptionservice.entity.OrderItemEntity;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private long orderitemId;
    private long orderlistId;
    private long product_id;
    private int orderprice;
    private int count;
    private ProductDTO products;


    public OrderItemDTO(OrderItemEntity orderItemEntity) {
        this.orderitemId = orderItemEntity.getOrderitemId();
        this.orderlistId = orderItemEntity.getOrderList().getOrderlistId();
        this.product_id = orderItemEntity.getProduct().getProductId();
        this.orderprice = orderItemEntity.getOrderPrice();
        this.count = orderItemEntity.getCount();
    }





}
