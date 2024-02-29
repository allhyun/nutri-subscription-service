package project3.nutrisubscriptionservice.dto;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import project3.nutrisubscriptionservice.config.WebSocketConfig;
import project3.nutrisubscriptionservice.entity.ChatMessageEntity;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.service.ChatService;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ChatMessageDTO{
    public enum MessageType{
        ENTER,TALK
    }
    private MessageType type;
    private Long chatId;
    private Long roomId;
    private String chatContents;
    private LocalDateTime createdAt;

//    public static ChatMessageDTO fromEntity(ChatMessageEntity chatMessage) {
//        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
//        return ChatMessageDTO.builder()
//                .chatId(chatMessage.getChat_id())
//                .roomId(chatMessage.getRoomid().getRoomid())
//                .chatContents(chatMessage.getChatContents())
//                .createdAt(chatMessage.getCreatedAt())
//                .build();
//
//    }

}
