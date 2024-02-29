package project3.nutrisubscriptionservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.service.ChatService;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    @Autowired
    ChatService chatService;



//    @PostMapping("/message")
//    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO){
//        ChatRoomEntity savedRoom = chatService.createRoom(chatMessageDTO.getRoomId());
//        return ResponseEntity.ok(savedRoom);
//    }

    @PostMapping
    public ChatRoomDTO createRoom(@RequestBody ChatRoomDTO chatRoomDTO){
        return chatService.createRoom(chatRoomDTO);
    }

//    @GetMapping
//    public List<ChatRoomDTO> findAllRoom(){
//        return chatService.findAll();
//    }


}
