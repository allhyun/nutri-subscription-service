package project3.nutrisubscriptionservice.dto;

import lombok.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import project3.nutrisubscriptionservice.entity.ChatMessageEntity;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.service.ChatService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long roomId;
    private Long userId;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public void handlerActions(WebSocketSession session, ChatMessageDTO chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessageDTO.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setChatContents(chatMessage.getChatId() + "님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);

    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}
