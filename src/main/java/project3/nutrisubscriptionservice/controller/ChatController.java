package project3.nutrisubscriptionservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project3.nutrisubscriptionservice.dto.ChatMessageDTO;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.service.ChatService;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    @Autowired
    ChatService chatService;



    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO){
        ChatRoomEntity savedRoom = chatService.createRoom(chatMessageDTO.getRoomId());
        return ResponseEntity.ok(savedRoom);
    }


}
