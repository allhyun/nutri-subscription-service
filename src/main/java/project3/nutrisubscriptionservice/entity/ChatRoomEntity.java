package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Table(name="chatroom")
public class ChatRoomEntity {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id", nullable = false)
    private long roomid;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 외래 키를 가지는 필드
    private UserEntity user;// UserEntity의 식별자를 참조하는 필드


    public static ChatRoomEntity of(Long roomId) {
        return ChatRoomEntity.builder()
                .roomid(roomId)
                .build();
    }
}
