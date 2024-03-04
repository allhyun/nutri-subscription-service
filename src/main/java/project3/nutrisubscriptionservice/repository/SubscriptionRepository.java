package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project3.nutrisubscriptionservice.entity.SubscriptionEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUser(UserEntity user);
}
