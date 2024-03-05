package project3.nutrisubscriptionservice.service;

import io.micrometer.core.instrument.Meter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.*;
import project3.nutrisubscriptionservice.entity.*;
import project3.nutrisubscriptionservice.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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


    ///////////////////////////////////////////////??//////
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
//        for(OrderItemEntity orderItem : orderItemEntities){
//            addOrderItemList(orderItem);
//        }
        order.setOrderItemEntitiy(orderItemEntities);
        order.setOrderdate(LocalDateTime.now());
        return order;
    }


    public static int getTotalPrice(){
        int totalPrice=0;
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        for(OrderItemEntity orderItemEntity : orderItemEntities){
            totalPrice += getTotalPrice();

        }return totalPrice;

    }

    //    public void orderCancle() {
//        List<OrderItemEntity> orderItems = new ArrayList<>();
//        for(OrderItemEntity orderItemEntity : orderItems){
//            orderItemEntity.cancle();
//        }
//    }
    //orderItemDTO&Entity
    //Entity
    public static OrderItemEntity createOrderItem(ProductEntity product, int count) {

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setProduct(product);
        orderItem.setCount(count);
        orderItem.setOrderPrice(product.getPPrice());

        return orderItem;
    }

    public int getTotaitemPrice() {
        OrderItemEntity orderItem = new OrderItemEntity();

        return orderItem.getOrderPrice()*orderItem.getCount();
    }

    //    public void cancel() {
//        OrderItemEntity orderItem = new OrderItemEntity();
//        orderItem.getProduct().addStock(count);
//    }
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

    //장바구니 상품주문
    public Long orders(List<OrderListDTO> orderDtoList, String email) {

        // 로그인한 유저 조회
        UserEntity user = userRepository.findByEmail(email);

        // orderDto 객체를 이용하여 item 객체와 count 값을 얻어낸 뒤, 이를 이용하여 OrderItem 객체(들) 생성
        List<OrderItemEntity> orderItem = new ArrayList<>();
        for (OrderListDTO orderDto : orderDtoList) {
            ProductEntity product = productRepository.findById(orderItem.get(0).getProduct().getProductId()).orElseThrow(EntityNotFoundException::new);
            OrderItemEntity orderItemEntity = createOrderItem(product, orderItem.get(0).getCount());
            orderItem.add(orderItemEntity);
        }

        //Order Entity 클래스에 존재하는 createOrder 메소드로 Order 생성 및 저장
        OrderListEntity order = createOrder(user, orderItem);
        orderListRepository.save(order);
        return order.getUser().getId();
    }




    ////////////////OrderListDTO////////////??//////

    //주문상품 추가
//    public static OrderItemEntity addOrderItem(OrderItemEntity orderItemEntity) {
//
//        List<OrderItemEntity> orderItems = new ArrayList<>();
//
//        orderItems.add(orderItemEntity);
//        orderItemEntity.setOrderList(orderItemEntity.getOrderList());
//
//        return orderItemEntity;
//    }

    //제품 주문하기
//    public OrderListEntity createOrderList(UserEntity user, List<OrderItemEntity> orderItemList){
//
//
//        OrderListEntity orderListEntity = new OrderListEntity( );
//        orderListEntity.setUser(user);
//        for(OrderItemEntity orderItemEntity : orderItemList){
//            addOrderItem(orderItemEntity);
//        }
//        return orderListEntity;
//    }


    //장바구니
//    public OrderItemEntity addCartOrder(Long itemId,long userId, Long productId,CartEntity cart,ProductEntity product, int price,int count){
//        UserEntity user = userRepository.findById(userId).orElse(null);
//        if (user == null) {
//            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
//        }
//
//        ProductEntity products = productRepository.findById(productId).orElse(null);
//        if (products == null) {
//            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
//        }
//
//        OrderItemEntity orderItem = OrderItemService.createOrderItem(itemId );
//    }


//    public int getTotalPrice(){
//        int totalPrice=0;
//        List<OrderItemDTO> orderItems = new ArrayList<>();
//        for(OrderItemDTO orderItemDTO :orderItems){
//            totalPrice += (orderItemDTO.getOrderprice()*orderItemDTO.getCount());
//        }
//
//
//        return totalPrice;
//    }

    ////////////////////////////////////////////

//    public static OrderItemEntity createOrderItem(ProductEntity product, int count){
//
//        OrderItemEntity orderItemEntity = new OrderItemEntity();
//        orderItemEntity.setProduct(product);
//        orderItemEntity.setCount(count);
//        orderItemEntity.setOrderPrice(product.getPPrice());
//
//        return orderItemEntity;
//    }
/////////////////////////////////////////////////////////////////
//////herehere............

//    public OrderListEntity ItemCheckout(UserEntity user,Long userId ){
//        Optional<UserEntity> userEntity = userRepository.findById(userId);
//        // 로그인이 되어있는 유저의 id와 주문하는 id가 같아야 함
//        // 사용자 ID로 장바구니 아이템을 조회합니다.
//        //Optional<CartEntity> cartItems = cartRepository.findByUser(userId);
//        List<CartProductEntity> cartItemss=cartProductRepository.findByUser(userId);
//        OrderListEntity order = new OrderListEntity();
//        order.setUser(user);
//        for(CartProductEntity cartProduct : cartItemss){
//            // 장바구니 항목을 주문 항목으로 변환합니다.
//            OrderItemEntity orderItem = new OrderItemEntity();
//            orderItem.setProduct(cartProduct.getProduct());
//            orderItem.setCount(cartProduct.getQuantity());
//            orderItem.setOrderList(order);
//
//            // 주문 항목을 추가합니다.
//            OrderItemService.addOrderItem(orderItem);
//        }
//
//        }
//
//    }
//
//
//
//
//
//    ///////////////////////////////OrderItemDTO  생성등 메서드들///////////////////////////
//
//    //개별 주문 상품
//    public static OrderItemEntity createOrderItem(int oid,ProductEntity product,OrderListEntity orderListEntity, int count){
//
//        OrderItemEntity orderItemEntity = new OrderItemEntity();
//        orderItemEntity.setOrderitemId(oid);
//        orderItemEntity.setProduct(product);
//        orderItemEntity.setOrderList(orderListEntity);
//        orderItemEntity.setCount(count);
//        orderItemEntity.setOrderPrice(product.getPPrice());
//
//        return orderItemEntity;
//    }
//
//    //장바구니 전체 주문
//    public static OrderItemEntity createcartOrderItem(int oid,ProductEntity product,CartProductEntity cartProduct){
//        OrderItemEntity orderItem = new OrderItemEntity();
//        orderItem.setOrderitemId(oid);
//        orderItem.setProduct(cartProduct.getProduct());
//        orderItem.setOrderPrice(cartProduct.getProduct().getPPrice());
//        orderItem.setCount(cartProduct.getQuantity());
//
//        return orderItem;
//
//    }
//
//
///////////////////////////////////service////////////////////////////////////
//
//    //주문 생성
//    public void createOrder(UserEntity userEntity){
//
//
//        OrderListEntity orderListEntity = new OrderListEntity();
//        orderListEntity.setUser(userEntity);
//        orderListRepository.save(orderListEntity);
//    }
//
//    //주문내역 추가
//    public void order(UserEntity userEntity, List<CartProductEntity> cartProductEntities){
//        List<OrderItemEntity> orderItemEntities = new ArrayList<>(); // 주문내역에 추가할 아이템 리스트
//
//        List<OrderItemService> orderservice=new ArrayList<>();
//        //장바구니의 각 상품을 주문아이템으로 변환하여 리스트에 추가(?)
//        for(CartProductEntity cartProduct : cartProductEntities){
//            OrderItemEntity orderItemEntity = createOrderItem(cartProduct.getProduct(),cartProduct.getQuantity());
//            orderItemEntities.add(orderItemEntity);//리스트에 주문아이템을 추가
//
//        }
//        //OrderListEntity orderListEntity = createOrderList(userEntity, orderItemEntities);
//        //orderListEntity.set->금엑 낧
//        orderListRepository.save(orderListEntity);
//    }
//
//
//
//    //전체주문조회
//
//    public List<OrderListEntity> orderListEntities(){
//        return orderListRepository.findAll();
//    }
//
//    //특정주문 조회
//    public OrderListEntity orderView (Long id){
//        return orderListRepository.findById(id).get();
//    }
//
//
//    //주문 수정
//    public void orderUPdate(Long id, OrderListEntity order){
//        OrderListEntity Uorder = orderListRepository.findById(id).get();
//        orderListRepository.save(Uorder);
//    }

    ///////////////////////////////////////////////////////////////////////////////
    public void  settingProductDTO(OrderItemDTO orderItemDTO){
        log.info("product id {}", orderItemDTO.getProducts());
        orderItemDTO.setProducts(productService.getProductById(orderItemDTO.getProduct_id()));
    }
/////////////////////////////////////////////////////////////////////

    public void addOrderitems(Long userid,Long productid,int count){
        log.info("id{},product id{}, count : {}",userid, productid, count);

        UserEntity user = userRepository.findById(userid).orElse(null);
        log.error("user {}", user.getName());
//        if (user == null) {
//            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
//        }

        ProductEntity product = productRepository.findById(productid).orElse(null);
        log.error("product {}", product.getPPrice());
//        if (product == null) {
//            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
//        }

        // 주문 리스트 확인 또는 새로 생성
        List<OrderListEntity> order = orderListRepository.findByUserId(userid);
//                .orElseGet(() -> {
//                    log.info("id{}",userid);
//                    OrderListEntity newOrder = OrderListEntity.builder().user(user).build();
//                    return orderListRepository.save(newOrder);
//                });
        log.error("order {}", order.get(0).getOrderdate());

        // 주문 상품 생성
        OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                .orderList((OrderListEntity) order)
                .product(product)
                .count(count)
                // 다른 필요한 필드들을 여기에 추가하세요
                .build();


        try {
            // 주문 상품 저장
            orderItemRepository.save(orderItemEntity);

        } catch ( Exception e) {
            log.error("error {}", e.getMessage());
        }
    }


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
            throw new NoSuchElementException("해당 상품을 찾을 수 없습니다.");
        }
        orderListRepository.delete(orderlist);
    }
}
