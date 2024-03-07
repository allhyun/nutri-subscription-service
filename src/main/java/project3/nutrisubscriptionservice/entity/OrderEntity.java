package project3.nutrisubscriptionservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project3.nutrisubscriptionservice.dto.ProductDTO;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="order_id",nullable = false)
    private  long   orderId;
    @ManyToOne
    @JoinColumn(name="id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductEntity product;

   @Column(name="total_price",nullable = false)
    private int totalPrice;

   @Column(name="address",length = 50,nullable = false)
    private String address;

    @Column(name="zipcode",length = 5,nullable = false)
    private String zipCode;

    @Column(name="phone",length = 13,nullable = false)
    private String phone;

    @Column(name="addressee",length = 10,nullable = false)
    private String addressee;

    @Column(name="pay_date",nullable = false)
    private LocalDateTime payDate;


}
