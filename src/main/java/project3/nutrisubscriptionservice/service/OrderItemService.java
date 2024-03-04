package project3.nutrisubscriptionservice.service;

import io.micrometer.core.instrument.Meter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.*;
import project3.nutrisubscriptionservice.entity.*;
import project3.nutrisubscriptionservice.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
//    OrderListEntity orderListEntity1;
//    @Autowired
//    CartProductDTO cartProductDTO;



//    public OrderListDTO getOrderByUser(Long userid) {
//        UserEntity userEntity = userRepository.findById(userid)
//                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다.: " + userid));
//        //OrderListEntity orderListEntity = orderListRepository.findByUserId(userid);
//        List<OrderListEntity> orderList = orderListRepository.findAllByUserId(userid);
//
//        // OrderListEntity에서 주문 항목 목록을 가져옴
//        List<OrderItemEntity> orderItemEntities = orderListEntity.getOrderItemEntitiy();
//
//        // OrderItemEntity 목록을 OrderItemDTO 목록으로 변환
//        List<OrderItemDTO> orderItemDTOList = orderItemRepository.findByOrderList(orderListEntity)
//                .stream()
//                .map(order -> {return new OrderItemDTO(order);})
//                .collect(Collectors.toList());
//        for(OrderItemDTO orderItemDTO : orderItemDTOList){
//            settingProductDTO(orderItemDTO);
//        }
//
//        OrderListDTO orderListDTO = new OrderListDTO();
//        return OrderListDTO.builder()
//                .orderlistId(orderListEntity.getOrderlistId())
//                .id(userEntity.getId())
//
//                .orderdate(orderListEntity.getOrderdate())
//                .orderItems(orderItemDTOList)
//                //.user(userEntity)
//                .build();
////                findById(userId);
//    }



    public OrderListDTO getOrderByUser(Long userId, int page, int size) {
        // UserEntity를 찾고, 예외 처리
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다.: " + userId));

        // 페이지네이션 파라미터를 사용하여 PageRequest 생성
        Pageable pageable = PageRequest.of(page, size);
        // 유저 ID에 해당하는 모든 OrderListEntity를 찾기
        Page<OrderListEntity> orderListEntityPage = orderListRepository.findAllByUserId(userId, pageable);

        // OrderListEntity가 없거나 여러 개 있는지 확인
        if (orderListEntityPage.isEmpty()) {
            throw new RuntimeException("주문 목록을 찾을 수 없습니다.");
        }

//        else if (orderListEntities.size() > 1) {
//            // TODO: 다수의 결과가 반환될 경우 처리 로직. 필요한 대응 방법에 따라 코드를 수정하세요.
//            throw new RuntimeException("여러 개의 주문 목록이 반환되었습니다.");
//        }

        // 첫 페이지의 첫 번째 주문 리스트만 가져옴 (이 부분은 비즈니스 요구사항에 따라 다르게 처리 가능)
        OrderListEntity orderListEntity = orderListEntityPage.getContent().get(0); // 단일 결과로 가정




        //OrderListEntity orderListEntity = orderListEntities.get(0); // 단일 결과로 가정

        // 주문 항목 목록 가져오기: 리팩토링하거나 'orderListEntity'를 사용하여 조회할 필요가 있는 경우, 이 부분을 수정
        List<OrderItemEntity> orderItemEntities = orderListEntity.getOrderItemEntitiy();

        // OrderItemEntity 목록을 OrderItemDTO 목록으로 변환
        List<OrderItemDTO> orderItemDTOList = orderItemRepository.findByOrderList(orderListEntity)
                .stream()
                .map(order -> new OrderItemDTO(order))
                .collect(Collectors.toList());

        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            settingProductDTO(orderItemDTO);
        }

        // OrderListDTO 객체 생성 및 설정
        // OrderListDTO 빌더 패턴 사용 (주의: 여기에 있는 `.id`는 UserEntity의 ID를 설정하는 것이 아닐 수 있음)
        return OrderListDTO.builder()
                .orderlistId(orderListEntity.getOrderlistId())
                .id(userEntity.getId())
                .orderdate(orderListEntity.getOrderdate())
                .orderItems(orderItemDTOList)
                // .user(userEntity) // 필요한 경우 UserEntity를 DTO에 포함시켜야 할 수 있습니다.
                .build();
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
    public void  settingProductDTO(OrderItemDTO orderItemDTO){
       log.info("product id {}", orderItemDTO.getProducts());
        orderItemDTO.setProducts(productService.getProductById(orderItemDTO.getProduct_id()));
    }
/////////////////////////////////////////////////////////////////////
}
