package project3.nutrisubscriptionservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="user")
public class UserEntity {
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private long id;

    @Column(name="email", nullable = false,length=50)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone",nullable = false, length = 13)
    private String phone;

    @Column(name = "zipcode",nullable = true, length = 5)
    private String zipcode;

    @Column(name = "address",nullable = true,  length = 255)
    private String address;

    @OneToMany(mappedBy = "user") // UserEntity를 참조하는 필드명
    private List<ChatRoomEntity> chatRooms;

}
