package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {


}
