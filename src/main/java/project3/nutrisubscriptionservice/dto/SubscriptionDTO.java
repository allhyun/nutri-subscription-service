package project3.nutrisubscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project3.nutrisubscriptionservice.entity.OrderListEntity;
import project3.nutrisubscriptionservice.entity.SubscriptionEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SubscriptionDTO {
    private long subId;
    private long userId;
    private long orderlistId;
    private LocalDateTime startDate;
    private LocalDateTime expDate;

    public SubscriptionDTO(SubscriptionEntity subscriptionEntity) {
        this.subId = subscriptionEntity.getSubId();
        this.userId = subscriptionEntity.getUser().getId();
        this.orderlistId = subscriptionEntity.getOrderList().getOrderlistId();
        this.startDate = subscriptionEntity.getStartDate();
        this.expDate = subscriptionEntity.getExpDate();
    }

}
