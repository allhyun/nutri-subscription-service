package project3.nutrisubscriptionservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import project3.nutrisubscriptionservice.dto.ChatMessageDTO;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.entity.ChatMessageEntity;
import project3.nutrisubscriptionservice.entity.ChatRoomEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.ChatService;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor

public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatService chatService;
    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();
    // chatRoomId: {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();


    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("{}", payload);
        ChatMessageDTO chatMessage = objectMapper.readValue(payload, ChatMessageDTO.class);

        ChatRoomDTO chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        chatRoom.handlerActions(session, chatMessage, chatService);
    }
}