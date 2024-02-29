package project3.nutrisubscriptionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.repository.ChatRoomRepository;

import java.io.IOException;
import java.util.*;

@Service
@Setter
@Component
@Slf4j
@AllArgsConstructor
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
//    @Autowired
//    SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDTO> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDTO findRoomById(Long roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoomDTO createRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        Long randomId = Long.valueOf(UUID.randomUUID().toString());
//        ChatRoomDTO chatRoom = ChatRoomDTO.builder()
//                .roomId(randomId)
//                .userId(user)
//                .build();
//        chatRooms.put(String.valueOf(randomId), chatRoom);
//        return chatRoom;
        chatRoomDTO.setRoomId(randomId);
        chatRooms.put(String.valueOf(randomId), chatRoomDTO);
        return chatRoomDTO;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
