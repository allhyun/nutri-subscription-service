package project3.nutrisubscriptionservice.dto;

import lombok.*;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import project3.nutrisubscriptionservice.entity.UserEntity;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserProfileDTO {
    private String email;
    private String name;
    private String phone;
    private String zipcode;
    private String address;
    public UserProfileDTO(){} //기본 생성자

    public static UserProfileDTO fromUser(UserEntity user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        return UserProfileDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .zipcode(user.getZipcode())
                .address(user.getAddress())
                .build();
    }

}