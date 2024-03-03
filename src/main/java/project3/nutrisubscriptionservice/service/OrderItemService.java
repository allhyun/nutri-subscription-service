package project3.nutrisubscriptionservice.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.*;
import project3.nutrisubscriptionservice.entity.OrderItemEntity;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.repository.OrderItemRepository;
import project3.nutrisubscriptionservice.repository.OrderListRepository;
import project3.nutrisubscriptionservice.repository.OrderRepository;
import project3.nutrisubscriptionservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Component
@Slf4j
@AllArgsConstructor
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderListRepository orderListRepository;
    @Autowired
    ProductRepository productRepository;
//    @Autowired
//    OrderItemEntity orderItemEntity;
//    @Autowired
//    OrderListEntity orderListEntity;
//    @Autowired
//    CartProductDTO cartProductDTO;


//    List<OrderItemDTO> orderItems = new ArrayList<>();
    //주문 생성
    public void createOrder(UserDTO userDTO){
        OrderListDTO orderListDTO = new OrderListDTO();
        orderListDTO.setUser(userDTO);
//        orderListRepository.save(orderListDTO);
    }

    //주문내역 추가
    public void order(UserDTO userDTO, List<CartProductDTO> cartProductDTOs){
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>(); // 주문내역에 추가할 아이템 리스트

        for(CartProductDTO cartProductDTO : cartProductDTOs){
            OrderItemDTO orderItemDTO = OrderItemService.createOrderItem(cartProductDTO.getProductId(),cartProductDTO.getQuantity());
            orderItemDTOs.add(orderItemDTO);
        }
//        OrderDTO orderDTO = OrderItemService.createOrder(userDTO,List<or>);
    }



    ///////////////////////////////생성등 메서드들///////////////////////////

    public void addOrderItemDto(OrderItemDTO orderItemDTO) {

        List<OrderItemDTO> orderItems = new ArrayList<>();

        orderItems.add(orderItemDTO);
        orderItemDTO.setOrderlistId(orderItemDTO.getOrderlistId());

    }

    public OrderListDTO createOrderList(UserDTO userDTO, List<OrderItemDTO> orderItemDTOList){
        OrderListDTO orderListDTO = new OrderListDTO( );
        orderListDTO.setId(userDTO.getId());
        for(OrderItemDTO orderItemDTO : orderItemDTOList){
            addOrderItemDto(orderItemDTO);
        }
        return orderListDTO;
    }

    public int getTotalPrice(){
        int totalPrice=0;
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for(OrderItemDTO orderItemDTO :orderItems){
            totalPrice += (orderItemDTO.getOrderprice()*orderItemDTO.getCount());
        }

        return totalPrice;
    }

    public static OrderItemDTO createOrderItem(long productDTO, int count){

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProducts(orderItemDTO.getProducts());
        orderItemDTO.setCount(count);
        orderItemDTO.setOrderprice(orderItemDTO.getOrderprice());

        return orderItemDTO;
    }

    //


    //////////////////////////////////////////////////////////////
//    public List<OrderItemEntity> findAll(){
//        return orderItemRepository.findAll();
//    }
}
