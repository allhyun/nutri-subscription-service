package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.entity.ChatMessageEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity,Long> {



}
