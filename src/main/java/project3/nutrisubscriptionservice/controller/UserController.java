package project3.nutrisubscriptionservice.controller;


import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.ChangePasswordDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.dto.UserProfileDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.service.UserProfileService;
import project3.nutrisubscriptionservice.service.UserService;

@Controller
@Getter
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserProfileService userProfileService;

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
                    .name(responseUser.getName())
                    .phone(responseUser.getPhone())
                    .zipcode(responseUser.getZipcode())
                    .address(responseUser.getAddress())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(HttpSession session, @RequestBody UserDTO userDTO) {
        try {
            UserEntity user = userService.login(userDTO.getEmail(), userDTO.getPassword());

            if(user == null) {
                throw new RuntimeException("login failed");
            }

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .id(user.getId())
                    .build();

            //log.info
            //log.error()
            log.warn("session id{}",session.getId());

            session.setAttribute("userId", user.getId());




            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        try {
            // 세션 무효화
            session.invalidate();
            return ResponseEntity.ok("로그아웃 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable("userId") Long userId) {

        // userId를 사용하여 UserProfileDTO를 조회하고 반환하는 코드
        UserProfileDTO userProfileDTO = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileDTO);
    }


    //회원정보 수정 및 업데이트
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable("userId") Long userId, @RequestBody UserProfileDTO userProfileDTO) {
        userProfileService.updateUserProfile(userId , userProfileDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    //비밀번호 수정?
    @PostMapping("/{userId}/password")
    public ResponseEntity<?> changePassWord(@PathVariable("userId")Long userId, @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            //ChangePasswordDTO chagePassword = new ChangePasswordDTO(changePasswordDTO.getCurrentPassword(), changePasswordDTO.getNewPassword());
            ChangePasswordDTO chagePassword = ChangePasswordDTO.builder()
                            .currentPassword(changePasswordDTO.getCurrentPassword())
                                    .newPassword(changePasswordDTO.getNewPassword())
                                            .build();
            userProfileService.changePassword(userId, chagePassword);

            // 변경된 비밀번호 정보를 새로운 ChangePasswordDTO 객체에 담아 반환합니다.
            ChangePasswordDTO updatedPasswordDTO = ChangePasswordDTO.builder()
                    .newPassword(changePasswordDTO.getNewPassword())
                    .build();
            return ResponseEntity.ok().body(updatedPasswordDTO); // 변경된 비밀번호 정보를 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}