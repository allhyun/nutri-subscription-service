package project3.nutrisubscriptionservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.OrderDTO;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
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

@RestController
@RequestMapping("/orderlist")
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

    // 사용자 ID 별 주문 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderListEntity>> findByMyUserId(@PathVariable Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for this id :: " + userId));

            List<OrderListEntity> orderHistory = orderListRepository.findByUser(user);

            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 주문 생성
    @PostMapping("/save")
    public ResponseEntity<OrderItemEntity> save(@RequestBody OrderItemEntity orderItemEntity) {
        OrderItemEntity savedOrder = orderItemService.addOrderItem(orderItemEntity);
        return ResponseEntity.ok(savedOrder);
    }


}
