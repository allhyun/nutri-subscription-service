package project3.nutrisubscriptionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.OrderListDTO;
import project3.nutrisubscriptionservice.dto.SubscriptionDTO;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.SubscriptionEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.OrderListRepository;
import project3.nutrisubscriptionservice.repository.SubscriptionRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    OrderListRepository orderListRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    // 주문 리스트 호출
    public List<OrderListDTO> getUserOrderLists(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<OrderListEntity> orderListEntities = orderListRepository.findByUser(user);
        return orderListEntities.stream()
                .map(OrderListDTO::new)
                .collect(Collectors.toList());
    }

    // 구독 정보 호출
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findByUser(user);
        return subscriptions.stream()
                .map(entity -> new SubscriptionDTO(
                        entity.getSubId(),
                        entity.getUser().getId(),
                        entity.getOrderList().getOrderlistId(),
                        entity.getStartDate(),
                        entity.getExpDate()))
                .collect(Collectors.toList());
    }

    public SubscriptionDTO saveSubscription(SubscriptionDTO subscriptionDTO) {
        UserEntity user = userRepository.findById(subscriptionDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        OrderListEntity orderList = orderListRepository.findById(subscriptionDTO.getOrderlistId())
                .orElseThrow(() -> new IllegalArgumentException("OrderList not found"));

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .subId(subscriptionDTO.getSubId())
                .user(user)
                .orderList(orderList)
                .startDate(subscriptionDTO.getStartDate())
                .expDate(subscriptionDTO.getExpDate())
                .build();

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);
        return new SubscriptionDTO(savedSubscription);
    }
}
