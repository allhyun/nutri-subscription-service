package project3.nutrisubscriptionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.dto.SubscriptionDTO;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.SubscriptionEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@Slf4j
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/orders")
    public List<OrderListDTO> getUserOrderLists(@PathVariable Long userId) {
        return subscriptionService.getUserOrderLists(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public List<SubscriptionDTO> getUserSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public SubscriptionDTO saveSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.saveSubscription(subscriptionDTO);
    }
}
