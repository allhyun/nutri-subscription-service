package project3.nutrisubscriptionservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.OrderDTO;
import project3.nutrisubscriptionservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orderlist")
@Slf4j
public class OrderListController {
    @Autowired
    OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> findByMyUserId(@PathVariable Long userId, Model model){
        List<OrderDTO> orderDTO = orderService.findByUserId(userId);
        model.addAttribute("user",orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    //주문하기
    @PostMapping("/save")
    public OrderDTO save( @RequestBody OrderDTO orderDTO){

        return orderService.save(orderDTO);
    }

}
