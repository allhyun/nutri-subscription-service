package project3.nutrisubscriptionservice.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.*;
import project3.nutrisubscriptionservice.entity.*;
import project3.nutrisubscriptionservice.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductService productService;
//    @Autowired
//    OrderItemEntity orderItemEntity;
//    @Autowired
//    OrderListEntity orderListEntity;
//    @Autowired
//    CartProductDTO cartProductDTO;



    public OrderListDTO getOrderByUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다.: " + userId));
        return OrderListDTO.builder().build();
//                findById(userId);
    }


    ////////////////OrderListDTO////////////??//////
    //주문상품 추가
    public OrderItemEntity addOrderItem(OrderItemEntity orderItemEntity) {

        List<OrderItemEntity> orderItems = new ArrayList<>();

        orderItems.add(orderItemEntity);
        orderItemEntity.setOrderList(orderItemEntity.getOrderList());

        return orderItemEntity;
    }

    //주문상품 추가
//    public OrderItemDTO addOrderItem(Long userId,Long orderitemId,OrderItemDTO orderItemDTO) {
//
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다.: " + userId));
//
//        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderitemId)
//                .orElseThrow(() -> new RuntimeException("주문상품을 찾을 수 없습니다.: " + orderitemId));
//        List<OrderItemEntity> orderItems = new ArrayList<>();
//
//        orderItems.add(orderItemEntity);
//        orderItemEntity.setOrderList(orderItemEntity.getOrderList());
//
//        return ;
//    }


//주문상품들 리스트화

    public OrderListEntity createOrderList(UserEntity user, List<OrderItemEntity> orderItemList){


        OrderListEntity orderListEntity = new OrderListEntity( );
        orderListEntity.setUser(user);
        for(OrderItemEntity orderItemEntity : orderItemList){
            addOrderItem(orderItemEntity);
        }
        return orderListEntity;
    }

    public int getTotalPrice(){
        int totalPrice=0;
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for(OrderItemDTO orderItemDTO :orderItems){
            totalPrice += (orderItemDTO.getOrderprice()*orderItemDTO.getCount());
        }

        return totalPrice;
    }








    ///////////////////////////////OrderItemDTO  생성등 메서드들///////////////////////////

    //개별 주문 상품
    public static OrderItemEntity createOrderItem(ProductEntity product, int count){

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(product);
        orderItemEntity.setCount(count);
        orderItemEntity.setOrderPrice(product.getPPrice());

        return orderItemEntity;
    }


/////////////////////////////////service////////////////////////////////////

    //주문 생성
    public void createOrderList(UserEntity userEntity){
//        UserEntity user = userRepository.findById(Id).orElseThrow(null);
//        if(user == null){
//            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
//        }
//        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderlistId).orElseThrow(null);
//        if(orderItemEntity == null){
//            throw new RuntimeException("해당 주문상품을 찾을 수 없습니다.");
//        }
//        List<OrderListEntity> orderList = orderListRepository.findByUser(Id)
//                .stream()
//                .map(order -> {return new OrderListEntity(order);})
//                .collect(Collectors.toList());
//        for(OrderListEntity orderListEntity : orderList) {
//
//            settingProductDTO(orderList) ;
//        }

        OrderListEntity orderListEntity = new OrderListEntity();
        orderListEntity.setUser(userEntity);
        orderListRepository.save(orderListEntity);
    }

    //주문내역 추가
    public void order(UserEntity userEntity, List<CartProductEntity> cartProductEntities){
        List<OrderItemEntity> orderItemEntities = new ArrayList<>(); // 주문내역에 추가할 아이템 리스트

        //장바구니의 각 상품을 주문아이템으로 변환하여 리스트에 추가(?)
        for(CartProductEntity cartProduct : cartProductEntities){
            OrderItemEntity orderItemEntity = OrderItemService.createOrderItem(cartProduct.getProduct(),cartProduct.getQuantity());
            orderItemEntities.add(orderItemEntity);//리스트에 주문아이템을 추가
                   // .stream()
                   // .map(order->{return new OrderItemEntity(order);})
                   // .collect(Collectors.toList());
        }
        // 주문 목록 생성
        OrderListEntity orderListEntity = createOrderList(userEntity, orderItemEntities);
        orderListRepository.save(orderListEntity);
    }

    //전체주문조회

    public List<OrderListEntity> orderListEntities(){
        return orderListRepository.findAll();
    }

    //특정주문 조회
    public OrderListEntity orderView (Long id){
        return orderListRepository.findById(id).get();
    }


    //주문 수정
    public void orderUPdate(Long id, OrderListEntity order){
        OrderListEntity Uorder = orderListRepository.findById(id).get();
        orderListRepository.save(Uorder);
    }

////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////
//
//    //유저 id로 주문 조회
//    public List<OrderListDTO> findByUser(long id){
//        List<OrderListDTO> orderListDTOList = orderListRepository.findByUser(id)
//                .stream()
//                .map(order -> {return new OrderListDTO(order);})
//                .collect(Collectors.toList());
//        for(OrderListDTO orderListDTO : orderListDTOList){
//            settingProductDTO()
//        }
//    }
//
//
//    public void  settingProductDTO(OrderListEntity orderListEntity){
//        log.info("product id {}", orderListEntity.getOrderItemEntitiy());
//        orderListEntity.setOrderItemEntitiy(order);
//    }
/////////////////////////////////////////////////////////////////////
}
