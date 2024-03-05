package project3.nutrisubscriptionservice.controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.*;
import project3.nutrisubscriptionservice.entity.OrderEntity;
import project3.nutrisubscriptionservice.entity.OrderItemEntity;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.OrderListRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.CartService;
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
    @Autowired
    CartService cartService;



    @GetMapping("/{userId}/order")

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
    // 단일 상품 주문
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/order/add")
    @ResponseBody
    public ResponseEntity order(@RequestBody @Valid OrderListDTO orderDto,
                                BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Long orderId;
        log.error("eeee {} ",  principal.getName());
        try {
            orderId = orderItemService.order(orderDto, principal.getName());
        } catch (Exception e) {
            log.error("Exception:", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


   @PostMapping(value = "/cart/orders")
   public ResponseEntity<Long> placeOrder(@RequestBody List<OrderListDTO> orderDtoList, Long userId, @AuthenticationPrincipal UserDetails userDetails) {
       try {
           // 현재 로그인한 사용자의 이메일 가져오기
           String email = userDetails.getUsername();

           // 주문을 생성하고 주문 ID를 가져옴
           Long orderId = orderItemService.orders(orderDtoList, userId);

//           // 주문 아이템 목록을 이용하여 주문 생성
//           Long userId = orderItemService.orderFromCart(orderDtoList, userId);

           return ResponseEntity.ok(orderId);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }
   }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long userId, @RequestParam Long productId,Long orderlistid) {
        orderItemService.removeOrderItem(userId, productId,orderlistid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cart/orderlist")
    @ResponseBody
    public ResponseEntity ordersFromCart(@RequestBody List<CartDTO> cartDTO, Principal principal) {
        try {
            // 현재 로그인한 사용자의 이메일 가져오기
            String email = principal.getName();


            // 장바구니 주문을 처리하고 주문 ID를 가져옴
            Long orderId = orderItemService.orderCartItem(cartDTO, email);
            log.info("email : {}" , orderItemService.orderCartItem(cartDTO, email));

            return ResponseEntity.ok(orderId);
        } catch (EntityNotFoundException e) {
            // 장바구니나 사용자를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("장바구니나 사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            // 예상치 못한 예외가 발생한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 처리 중에 오류가 발생했습니다.");
        }
    }










}
