package project3.nutrisubscriptionservice.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long roomId;
    private Long userId;
    private Set<WebSocketSession> sessions = new HashSet<>();



    public void sendMessage(TextMessage message) {
        this.getSessions()
                .parallelStream()
                .forEach(session -> sendMessageToSession(session, message));
    }

    private void sendMessageToSession(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void join(WebSocketSession session) {
        sessions.add(session);
    }

    public static ChatRoomDTO fromEntity(ChatRoomEntity chatRoom) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomid())
                .userId(chatRoom.getUser().getId())
                .build();

    }
}
