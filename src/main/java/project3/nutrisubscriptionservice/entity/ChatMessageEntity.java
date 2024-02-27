package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="chatmessage")
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false) // optional 속성을 false로 설정하여 null 값을 허용하지 않음
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoomEntity roomid;

    @Column(name = "chat_text", nullable = false)
    private String chatContents;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ChatMessageEntity(ChatRoomEntity roomid, String chatContents, LocalDateTime createdAt) {
        this.roomid = roomid;
        this.chatContents = chatContents;
        this.createdAt = createdAt;
    }

}
