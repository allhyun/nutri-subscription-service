package project3.nutrisubscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project3.nutrisubscriptionservice.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
//@Builder
@Setter
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

    private List<ProductDTO> products;

    public  OrderDTO(OrderEntity orderEntity) {
        this.order_id = orderEntity.getOrderId();
        this.total_price = orderEntity.getTotalPrice();
        this.address = orderEntity.getAddress();
        this.zipcode = orderEntity.getZipCode();
        this.phone = orderEntity.getPhone();
        this.addressee = orderEntity.getAddressee();
        this.paydate = orderEntity.getPayDate();
    }


}