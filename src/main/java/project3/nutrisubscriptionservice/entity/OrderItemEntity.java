package project3.nutrisubscriptionservice.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="orderitem")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="orderitem_id",nullable = false)
    private long orderitemId;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name="orderlist_id")
    private OrderListEntity orderList;

    @Column(name="order_price",nullable = false)
    private int orderPrice;

    @Column(name="count",nullable = false)
    private int count;

    public OrderItemEntity(OrderItemEntity order) {
    }
}
