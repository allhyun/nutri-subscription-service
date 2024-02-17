package project3.nutrisubscriptionservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name="user")
public class UserEntity {
    @Id//pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    @Column(name="email", nullable = false,length=50)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone", length = 13)
    private String phone;

    @Column(name = "zipcode", length = 5)
    private String zipcode;

    @Column(name = "address", length = 255)
    private String address;

}
