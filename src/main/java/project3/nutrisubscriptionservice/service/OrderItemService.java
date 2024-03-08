package project3.nutrisubscriptionservice.service;


import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    OrderListRepository orderListRepository;//주문객체를 저장해야함
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository; //유저를 불러와서 연결해야함.
    @Autowired
    ProductService productService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartProductRepository cartProductRepository;
    @Autowired
    CartProductService cartProductService;




    ////////////////주문목록조회//////////////////////////
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

        // 첫 페이지의 첫 번째 주문 리스트만 가져옴 (이 부분은 비즈니스 요구사항에 따라 다르게 처리 가능)
        OrderListEntity orderListEntity = orderListEntityPage.getContent().get(0); // 단일 결과로 가정


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

//////////////////////////////////////////////////////////////////////////////////

    //orderListDTO&Entity
    //DTO
    public void addOrderItemDto(OrderItemDTO orderItemDTO){
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderItemDTOList.add(orderItemDTO);
    }
    //Entity
    public static void addOrderItemList(OrderItemEntity orderItemEntity){
        List<OrderItemEntity> orderITems=new ArrayList<>();
        orderITems.add(orderItemEntity);
        orderItemEntity.setOrderPrice(orderItemEntity.getOrderPrice());
        orderItemEntity.setOrderitemId(orderItemEntity.getOrderitemId());
        orderItemEntity.setProduct(orderItemEntity.getProduct());
        orderItemEntity.setOrderList(orderItemEntity.getOrderList());
        orderItemEntity.setOrderPrice(orderItemEntity.getOrderPrice());
        orderItemEntity.setCount(orderItemEntity.getCount());

    }

    public static OrderListEntity createOrder(UserEntity user,List<OrderItemEntity> orderItemEntities){
        OrderListEntity order = new OrderListEntity();
        order.setUser(user);
//        }
        order.setOrderItemEntitiy(orderItemEntities);
        order.setOrderdate(LocalDateTime.now());
        return order;
    }
    //orderItemDTO&Entity
    //Entity
    public static OrderItemEntity createOrderItem(ProductEntity product, int count) {

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setProduct(product);
        orderItem.setCount(count);
        orderItem.setOrderPrice(product.getPPrice()*count);

        return orderItem;
    }

    public static int getTotaitemPrice() {
        OrderItemEntity orderItem = new OrderItemEntity();

        return orderItem.getOrderPrice()*orderItem.getCount();
    }


    ////////////////////단일상품 주문
    public Long order(OrderListDTO orderList, String userId) {

        // OrderItem(List) 객체 생성
        List<OrderItemEntity> orderItemList = new ArrayList<>();
        for(OrderItemDTO orderItemDTO : orderList.getOrderItems()) {
            ProductEntity product = productRepository.findById(orderItemDTO.getProduct_id()).orElseThrow(EntityNotFoundException::new);
            orderItemList.add(createOrderItem(product, orderItemDTO.getCount()));
        }
        // OrderList 객체 생성

        UserEntity user = userRepository.findById(Long.valueOf(userId)).orElseThrow();
        OrderListEntity orderListEntity =  createOrder(user, orderItemList);

        log.error("orderListEntity {}", orderListEntity);

        log.info("id 1 {}",user.getId());
        log.info("product 1 {}",user.getId());

        // orderListEntity에 id 설정
        orderListEntity.setUser(user); // user 객체로 설정
        log.info("id 2 {}",orderListEntity.getUser().getId());

        // orderListEntity에 orderdate와 id 설정
        orderListEntity.setOrderdate(LocalDateTime.now()); // 현재 날짜와 시간으로 설정 혹은 필요에 따라 다른 방법 사용

        // OrderList 객체 DB 저장 (Cascade로 인해 OrderItem 객체도 같이 저장)
        orderListRepository.save(orderListEntity);

        // OrderListEntity의 id 값 가져오기
        Long orderListId = orderListEntity.getOrderlistId();

        // OrderItemEntity에 orderlist_id 설정 및 저장
        for (OrderItemEntity orderItem : orderItemList) {
            orderItem.setOrderList(orderListEntity); // OrderItemEntity의 orderlist_id 설정
            orderItemRepository.save(orderItem); // OrderItemEntity 저장
        }
        return orderListId;
    }
    // 주문 취소
//    public void orderCancel(Long orderId) {
//        OrderListEntity order = orderListRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
//        orderCancle();
//    }

    // 장바구니 상품(들) 주문
    public Long orderCartItem(List<CartDTO> cartDTO, String userId) {

        // 주문을 생성할 상품 목록을 담을 리스트
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CartDTO cartDto : cartDTO) {
            CartEntity cartEntity = cartRepository.findById(cartDto.getCartId())
                    .orElseThrow(EntityNotFoundException::new);

            // 각 장바구니에서 상품을 가져와서 주문 항목으로 변환하여 리스트에 추가
            for (CartProductEntity cartProduct : cartEntity.getCartProducts()) {
                OrderItemEntity orderItem = new OrderItemEntity();
                // 필요한 정보를 설정 (예: 상품, 수량 등)
                orderItem.setProduct(cartProduct.getProduct());
                orderItem.setOrderPrice(cartProduct.getProduct().getPPrice()*cartProduct.getQuantity());
                orderItem.setCount(cartProduct.getQuantity());
                // 생성된 주문 항목을 리스트에 추가
                orderItems.add(orderItem);
            }
            // 장바구니에서 상품을 주문한 후 장바구니 삭제
            cartRepository.delete(cartEntity);
        }

        // 주문 생성 및 저장
        UserEntity user = userRepository.findById(Long.valueOf(userId)).orElseThrow();


        OrderListEntity orderListEntity =  createOrder(user,orderItems);
        // orderListEntity에 id 설정
        orderListEntity.setUser(user); // user 객체로 설정
        log.info("id 2 {}",orderListEntity.getUser().getId());

        // orderListEntity에 orderdate와 id 설정
        orderListEntity.setOrderdate(LocalDateTime.now()); // 현재 날짜와 시간으로 설정 혹은 필요에 따라 다른 방법 사용

        // OrderList 객체 DB 저장 (Cascade로 인해 OrderItem 객체도 같이 저장)
        orderListRepository.save(orderListEntity);

        // OrderListEntity의 id 값 가져오기
        Long orderListId = orderListEntity.getOrderlistId();

//        Long cartId = cartEntity.getCartId();
        // OrderItemEntity에 orderlist_id 설정 및 저장
        for (OrderItemEntity orderItemss : orderItems) {
            orderItemss.setOrderList(orderListEntity); // OrderItemEntity의 orderlist_id 설정
            orderItemRepository.save(orderItemss); // OrderItemEntity 저장
        }
        return orderListId;

    }
    ///////////////////////////////////////////////////////////////////////////////
    public void  settingProductDTO(OrderItemDTO orderItemDTO){
        log.info("product id {}", orderItemDTO.getProducts());
        orderItemDTO.setProducts(productService.getProductById(orderItemDTO.getProduct_id()));
    }
/////////////////////////////////////////////////////////////////////



    public void removeOrderItem(Long userId,Long productId,Long orderlistid){
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
        }

        CartEntity cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            throw new NoSuchElementException("장바구니가 비어있습니다.");
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
        }
        OrderListEntity orderlist = orderListRepository.findById(orderlistid).orElse(null);
        if (orderlist == null) {
            throw new NoSuchElementException("해당 주문을 찾을 수 없습니다.");
        }
        orderListRepository.delete(orderlist);
    }
}
