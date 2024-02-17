package project3.nutrisubscriptionservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserDTO {

    private long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String zipcode;
    private String address;
}
