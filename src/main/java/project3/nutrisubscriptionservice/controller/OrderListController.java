package project3.nutrisubscriptionservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.OrderDTO;
import project3.nutrisubscriptionservice.dto.OrderItemDTO;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.entity.OrderEntity;
import project3.nutrisubscriptionservice.entity.OrderItemEntity;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.OrderListRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.OrderItemService;
import project3.nutrisubscriptionservice.service.OrderService;
import project3.nutrisubscriptionservice.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderliness")
@Slf4j
public class OrderListController {
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderListRepository orderListRepository;
    @Autowired
    UserService userService;



    @GetMapping("/{userId}/order")
//    public ResponseEntity<?> getMyOrders(@PathVariable Long userId) {
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId)); // 현재 로그인된 유저 이름 가져오기
//
//        // 주문 서비스를 이용하여 해당 유저의 주문 내역 조회
//        List<OrderListEntity> orderHistory = orderListRepository.findByUser(user);
//
//        // Entity 목록을 DTO 목록으로 변환
//        List<OrderListDTO> orderListDTOs = orderHistory.stream()
//                .map(this::convertToOrderListDTO)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(orderListDTOs);
//
//        // 주문 내역이 없는 경우 적절한 상태 메시지 반환
//        if (orders.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(orders);
//    }
//    }

    public ResponseEntity<OrderListDTO> getUserOrders(@PathVariable Long userId,
                                                      @RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "size", required = false) Integer size,
                                                      Model model) {

        // 요청으로부터 받은 page와 size 파라미터 값이 없을 경우 기본값 설정
        int currentPage = (page != null) ? page : 0; // 예로써 기본 페이지 값으로 '0' 사용
        int pageSize = (size != null) ? size : 10;   // 예로써 기본 페이지 크기로 '10' 사용

        // try {
            OrderListDTO OrderList = orderItemService.getOrderByUser(userId,currentPage,pageSize);
            model.addAttribute("orderlist", OrderList);
            return ResponseEntity.ok(OrderList);

//            if (OrderList==null) {
//                throw new RuntimeException("login failed");
//            }
//            OrderListDTO orderList = OrderListDTO.builder()
//                        .orderlistId(OrderListEntity.get().getOrderlistId())
//                        .orderdate(OrderListEntity.get().getOrderdate())
//                        .orderItems(OrderListEntity.get().getOrderItemEntitiy()
//                                .stream()
//                                .map(orderItemEntity -> OrderItemDTO.builder()
//                                        .orderitemId(orderItemEntity.getOrderitemId())
//                                        .orderprice(orderItemEntity.getOrderPrice())
//                                        .build())
//                                .collect(Collectors.toList()))
//                        .build();
//                return ResponseEntity.ok(orderList);
//             else {
//                log.info("주문 목록이 없음 or 조회 실패: userId = {}", userId);
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e){
//            log.info("주문 목록이 없음 or 조회 실패: userId = {}, error={}", userId, e.getMessage(),e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//
//        }
    }


    // 사용자 ID 별 주문 조회
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<OrderListEntity>> findByMyUserId(@PathVariable Long userId) {
//        try {
//            UserEntity user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId));
//
//            List<OrderListEntity> orderHistory = orderListRepository.findByUser(user);
//
//            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    public ResponseEntity<?> findByMyUserId(@PathVariable Long userId) {
//        try {
//            UserEntity user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId));
//
//            List<OrderListEntity> orderHistory = orderListRepository.findByUser(user);
//
//            OrderListDTO orderHistoryDtoList = (OrderListDTO) orderHistory.stream()
//                    .map(order->{return new OrderListDTO(order);})
//                    .collect(Collectors.toList());
//
//            OrderListDTO orderListDTO = OrderListDTO.builder()
//                    .orderlistId(orderHistoryDtoList.getOrderlistId())
//                    .id(orderHistoryDtoList.getId())
//                    .orderItems(orderHistoryDtoList.getOrderItems())
//                    .orderdate(orderHistoryDtoList.getOrderdate())
//                    .build();
//
//            return ResponseEntity.ok().body(orderListDTO);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    // 주문 생성
    @PostMapping("/save")
    public ResponseEntity<OrderItemEntity> save(@RequestBody OrderItemEntity orderItemEntity) {
        OrderItemEntity savedOrder = orderItemService.addOrderItem(orderItemEntity);
        return ResponseEntity.ok(savedOrder);
    }


}
