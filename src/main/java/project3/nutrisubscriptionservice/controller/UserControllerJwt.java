package project3.nutrisubscriptionservice.controller;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.security.TokenProvider;
import project3.nutrisubscriptionservice.service.UserService;

@RestController
@Getter
@RequestMapping("/jwt")
public class UserControllerJwt {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    TokenProvider tokenProvider;

    @GetMapping("")
    public String getUser() {return "Get/User";}

    @PostMapping("/jwtlogin")
    public ResponseEntity<?> loginUser(HttpSession session, @RequestBody UserDTO userDTO){

        try {
            UserEntity user = userService.login(userDTO.getEmail(), userDTO.getPassword());

            if(user == null) {
                throw new RuntimeException("login failed");
            }
            //token 사용하기
            String token = tokenProvider.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .id(user.getId())
                    .phone(user.getPhone())
                    .zipcode(user.getZipcode())
                    .address(user.getAddress())
                    .token(token)
                    .build();



            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
