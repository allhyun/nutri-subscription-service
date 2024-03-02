package project3.nutrisubscriptionservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project3.nutrisubscriptionservice.dto.OrderDTO;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.dto.UserProfileDTO;
import project3.nutrisubscriptionservice.entity.OrderEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.OrderRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserProfileService userProfileService;

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    //주문목록 조회
    @Transactional(readOnly = true)//데이터베이스의 상태를 변경하는 작업 또는 한번에 수행되어야 하는 연산들
    public List<OrderDTO> findAll(){
        List<OrderDTO> orderDTOList = orderRepository.findAll().stream()
                .map(order -> {return new OrderDTO(order);}).toList();

        for(OrderDTO orderDTO : orderDTOList){
            settingProductDTO(orderDTO);
        }
        return orderDTOList;
    }

    //주문번호로 주문 조회
    public OrderDTO findById(long order_id){
        //order_id  로 주문정보 조회
        OrderEntity orderEntity = orderRepository.findById(order_id)
                .orElseThrow(()->new IllegalArgumentException("해당 주문번호가 없습니다. order_id="+order_id));

        //orderDTO생성
        OrderDTO orderDTO = new OrderDTO(orderEntity);

        settingProductDTO(orderDTO);
        return  orderDTO;
    }

    //유저의 id로 주문목록 조회
    @Transactional(readOnly = true)
    public List<OrderDTO> findByUserId(long id){
        List<OrderDTO> orderDTOList = orderRepository.findByUserId(id)
                .stream()
                .map(order -> {return new OrderDTO(order);}).collect(Collectors.toList());
        for(OrderDTO orderDTO : orderDTOList){
            settingProductDTO(orderDTO);
        }
        return orderDTOList;
    }

    //주문하기
    public OrderDTO save(OrderDTO orderDTO){
        //주문 상품 검증i
        productService.getProductById(orderDTO.getProduct_id());
        //현재 인증된 userId조회

        UserDTO userDTO = new UserDTO(userService.getMyUserWithAuthorities().get());
        Long userId = userDTO.getId();
        log.info("유저 저장"+userId);
        //현재 인증된 사용자 정보 조회
//        Optional<UserEntity> myUserOpt = userService.getMyUserWithAuthorities();
       // if (myUserOpt.isEmpty()) {
            // 인증된 사용자 정보가 없다면 사용자 정의 예외를 던지거나 적절한 처리
//            throw new RuntimeException("사용자를 찾을 수 없습니다.");
//        }
//        UserDTO userDTO = new UserDTO(myUserOpt.get());
//        Long userId = userDTO.getId();
        log.info("User saved: " + userId);



        OrderEntity orderEntity = OrderEntity.builder()
                .totalPrice(orderDTO.getTotal_price())
                .address(orderDTO.getAddress())
                .zipCode(orderDTO.getZipcode())
                .phone(orderDTO.getPhone())
                .addressee(orderDTO.getAddressee())
                .payDate(orderDTO.getPaydate())
                // 인증된 사용자를 주문에 연결
                //.user(userDTO.toEntity()) // toEntity 메서드는 UserDTO를 User 엔티티로 변환해주는 메서드
                .build();

        orderEntity = orderRepository.save(orderEntity);

        orderDTO = new OrderDTO(orderEntity);
        settingProductDTO(orderDTO);

        return  orderDTO;
    }



    @Transactional(readOnly = true)
//    public void  settingProductDTO(OrderDTO orderDTO){
//        orderDTO.setProducts(productService.getAllProducts());
//    }

    public void  settingProductDTO(OrderDTO orderDTO){
        log.info("product id {}", orderDTO.getProduct_id());
        orderDTO.setProducts(productService.getProductById(orderDTO.getProduct_id()));
    }


//    public void settingProductDTO(OrderDTO orderDTO) {
//        List<Long> productIds = orderDTO.getProduct_id(); // 주문에 속한 모든 제품 ID를 가져온다고 가정
//        List<ProductDTO> productDTOs = new ArrayList<>();
//
//        for (Long productId : productIds) {
//            ProductDTO productDTO = productService.getProductById(productId); // 각 제품 ID에 해당하는 ProductDTO를 가져옴
//            productDTOs.add(productDTO); // 리스트에 추가
//        }
//
//        orderDTO.setProducts(productDTOs); // OrderDTO에 제품 리스트 설정
//    }
}
