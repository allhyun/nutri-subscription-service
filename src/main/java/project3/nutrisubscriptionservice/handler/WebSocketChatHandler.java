package project3.nutrisubscriptionservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import project3.nutrisubscriptionservice.dto.ChatRoomDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.ChatService;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor

public class WebSocketChatHandler extends TextWebSocketHandler {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatService chatService;
    // 사용자 id와 웹소켓 세션을 매핑하는 hashmap
    private Set<WebSocketSession> sessions = new HashSet<>();
    private Map<String, WebSocketSession> chatRoomSessionMap = new HashMap<String, WebSocketSession>();
    //최초 연결 시


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓 연결 요청 시 URL에서 사용자 ID를 추출
        Long userId = getUserIdFromSession(session);

        if (userId != null) {
            log.info("{} 연결 성공, 사용자 ID: {}", session.getId(), userId);
            session.sendMessage(new TextMessage("웹소켓 연결 성공, 사용자 ID: " + userId));
        } else {
            session.sendMessage(new TextMessage("사용자 정보가 없습니다."));
        }
    }

    // WebSocketSession에서 사용자 ID를 추출하는 메서드
    private Long getUserIdFromSession(WebSocketSession session) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(Objects.requireNonNull(session.getUri()).toString()).build();
            String uidString = uriComponents.getQueryParams().getFirst("uid");
            if (uidString != null) {
                return Long.parseLong(uidString);
            }
        } catch (NumberFormatException e) {
            // 사용자 ID를 Long 타입으로 파싱할 수 없는 경우
            log.error("사용자 ID를 추출하는 데 실패했습니다.", e);
        }catch (NullPointerException e) {
            // URI가 null인 경우
            log.error("WebSocket 세션의 URI가 null입니다.", e);
        }
        return null;


//        String userid = searchUserId(session);
//        // 세션맵에 사용자 id와 웹소켓 세션을 추가
//        chatRoomSessionMap.put(userid,session);
//
//        // 웹소켓 세션의 URI를 파싱하여 uid를 얻어냄
//        UriComponents uriComponents =
//                UriComponentsBuilder.fromUriString(Objects.requireNonNull(session.getUri()).toString()).build();
//        String uidString = uriComponents.getQueryParams().getFirst("uid");
//        // uidString가 null이 아닌 경우에만 처리
//        if (uidString != null) {
//            Long uid = Long.parseLong(uidString); // 문자열 uid를 Long 타입으로 변환
//
//            // uid를 통해 사용자를 찾음
//            UserEntity user = userRepository.findById(uid).orElse(null);// 사용자가 존재하지 않을 경우 null 반환
//
//            // 사용자가 존재하는 경우
//            if(user != null) {
//                // 사용자의 ID를 얻어냄
//                Long userId = user.getId();
//
//                // 사용자의 ID를 로그에 출력
//                log.info("{} 연결 성공", session.getId());
//                // 웹소켓 연결 성공 메시지 전송
//                session.sendMessage(new TextMessage("웹소켓 연결 성공 session :" + session));
//            } else {
//                // 해당 사용자가 존재하지 않는 경우, 오류 메시지를 전송
//                session.sendMessage(new TextMessage("존재하지 않는 사용자입니다."));
//            }
//        } else {
//            // uidString이 null인 경우, 오류 메시지를 전송
//            session.sendMessage(new TextMessage("유효하지 않은 요청입니다."));
//        }


//        Long uid = Long.parseLong(uidString); // 문자열 uid를 Long 타입으로 변환
//
//        // uid를 통해 사용자를 찾음
//        UserEntity user = userRepository.findById(uid)
//                .orElse(null);// 사용자가 존재하지 않을 경우 null 반환
//        // 사용자가 존재하는 경우
//        if(user != null) {
//            // 사용자의 ID를 얻어냄
//            Long userId = user.getId();
//            log.info("{}연결 성공",session.getId());
//
//        } else {
//            // 해당 사용자가 존재하지 않는 경우, 오류 메시지를 전송
//            session.sendMessage(new TextMessage("존재하지 않는 사용자입니다."));
//        }
        // 해당 사용자가 존재하는 경우
//        try{
//            Long userId = user.getId();
//            log.info("{}연결 성공",session.getId());
//            session.sendMessage(new TextMessage("웹소켓 연결 성공 session :" + session   ));
//            // 사용자의 ID를 얻어냄
//
//
//        }catch (Exception e) {
//            log.error(e.getMessage());
//        }

    }

    //양방향 데이터 통신할 떄 해당 메서드가 call 된다.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //클라이언트가 WebSocket으로 메세지를 보낼 때 처리하는 로직
        ChatRoomDTO chatRoomDTO = (ChatRoomDTO) session.getAttributes().get("chatRoomDTO");
        if(chatRoomDTO == null){
            //사용자가 인증되지 않은 경우 처리할 로직
            String errorMessege = "로그인을 해주세요"; //오류 메세지
            TextMessage errorMessegeTextMessage = new TextMessage(errorMessege); // 오류 메시지를 TextMessage로 변환
            session.sendMessage(errorMessegeTextMessage); // 클라이언트에게 오류 메시지 전송
            return ;
        }
        handleMessage(session,message);
    }

    //웹소켓 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("{} 연결 종료",session.getId());
        sessions.remove(session);
    }

    //통신 에러 발생 시
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {


    }

    // 웹소켓 세션의 URI를 파싱하여 사용자 id를 찾아내는 메소드
    public String searchUserId(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(session.getUri().toString()).build();
        return uriComponents.getQueryParams().getFirst("uid");
    }
}



