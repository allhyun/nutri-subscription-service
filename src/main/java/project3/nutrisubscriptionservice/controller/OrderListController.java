package project3.nutrisubscriptionservice.controller;


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


//    @PostMapping(value = "/cart/orders")
//    @ResponseBody
//    public ResponseEntity orders(@RequestBody CartDTO cartDto, Principal principal) {
//
//        List<CartProductDTO> cartProductsDTO = cartDto.getCartProducts();
//
//        if (cartProductsDTO == null || cartProductsDTO.isEmpty()) {
//            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 장바구니 주문 상품들을 각각 검증
////        for (CartProductDTO cartOrderDto1 : cartProductsDTO) {
////            if (!cartService.validateCartItem(cartOrderDto1.get(), principal.getName())) {
////                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
////            }
////        }
//
//        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
//        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
//    }
//






  @PostMapping("/{userId}/add")
     public ResponseEntity<Void> addOrderitems(@PathVariable Long userId, @RequestBody OrderItemDTO orderItemDTO) {
        try {
           orderItemService.addOrderitems(userId,orderItemDTO.getProduct_id(), orderItemDTO.getCount());
            return ResponseEntity.ok().build();
         } catch (Exception e) {
           return ResponseEntity.notFound().build();
         }
       }





}
