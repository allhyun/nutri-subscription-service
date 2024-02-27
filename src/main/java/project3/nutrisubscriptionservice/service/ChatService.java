package project3.nutrisubscriptionservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.ChatMessageDTO;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.entity.ChatMessageEntity;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Setter
@AllArgsConstructor
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public List<ChatRoomEntity>findAll(){
        return chatRoomRepository.findAll();
    }

    public Optional<ChatRoomEntity> findRoomById(Long roomId){
        return chatRoomRepository.findById(roomId);
    }

    public ChatRoomEntity createRoom(Long roomId) {
        return chatRoomRepository.save(ChatRoomEntity.of(roomId));
    }

}
