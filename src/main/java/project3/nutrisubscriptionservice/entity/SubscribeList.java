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
@Table(name="subscribelist")
public class SubscribeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sub_id",nullable = false)
    private long  subId;

    @ManyToOne
    @JoinColumn(name="id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="order_id")
    private OrderEntity order;

    @Column(name="sub_Date",nullable = false)
    private LocalDateTime subDate;

    @Column(name="start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name="exp_date",nullable = false)
    private LocalDateTime expDate;



}
