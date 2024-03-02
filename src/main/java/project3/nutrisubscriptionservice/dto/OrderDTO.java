package project3.nutrisubscriptionservice.dto;

import lombok.*;
import project3.nutrisubscriptionservice.entity.OrderEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
//@Builder
@Setter

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private long order_id;
    private long id;
    private long product_id;
    private int total_price;
    private String address;
    private  String zipcode;
    private String phone;
    private String addressee;
    private LocalDateTime paydate;
//    private List<ProductDTO> products;
    private ProductDTO products;
//    public OrderDTO(){}

    public  OrderDTO(OrderEntity orderEntity) {
        this.id=orderEntity.getUser().getId();
        this.product_id= orderEntity.getProduct().getProductId();
        this.order_id = orderEntity.getOrderId();
        this.total_price = orderEntity.getTotalPrice();
        this.address = orderEntity.getAddress();
        this.zipcode = orderEntity.getZipCode();
        this.phone = orderEntity.getPhone();
        this.addressee = orderEntity.getAddressee();
        this.paydate = orderEntity.getPayDate();
    }



}
