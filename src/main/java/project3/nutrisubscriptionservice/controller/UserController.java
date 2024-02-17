package project3.nutrisubscriptionservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.service.UserService;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("")
    public String getUser() { return "GET/user";}

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try{
            UserEntity user=UserEntity.builder()
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .name(userDTO.getName())
                    .phone(userDTO.getPhone())
                    .zipcode(userDTO.getZipcode())
                    .address(userDTO.getAddress())
                    .build();
            UserEntity responseUser = userService.create(user);

            UserDTO responseUserDTO = userDTO.builder()
                    .email(responseUser.getEmail())
                    .password(responseUser.getPassword())
                    .id(responseUser.getId())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
