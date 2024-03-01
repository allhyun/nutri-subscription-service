package project3.nutrisubscriptionservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import project3.nutrisubscriptionservice.entity.UserEntity;

@Getter
@Builder
//@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    private long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String zipcode;
    private String address;

    public UserDTO(UserEntity userEntity) {
        this.id=userEntity.getId();
        this.email=userEntity.getEmail();
        this.password=userEntity.getPassword();
        this.name=userEntity.getName();
        this.phone=userEntity.getPhone();
        this.zipcode=userEntity.getZipcode();
        this.address=userEntity.getAddress();

    }


}

