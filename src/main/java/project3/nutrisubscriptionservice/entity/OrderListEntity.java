package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orderlist")
public class OrderListEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="orderlist_id", nullable = false)
    private long  orderlistId;

    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "orderList" , cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItemEntitiy = new ArrayList<>();


    @Column (name="orderdate",nullable = false)
    private LocalDateTime orderdate;


    public OrderListEntity(OrderListEntity order) {
    }
}
