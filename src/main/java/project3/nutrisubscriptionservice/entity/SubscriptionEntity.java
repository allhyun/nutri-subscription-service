package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="subscription")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sub_id",nullable = false)
    private long  subId;

    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="orderlist_id", nullable = false)
    private OrderListEntity orderList;

    @Column(name="start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name="exp_date",nullable = false)
    private LocalDateTime expDate;

}
