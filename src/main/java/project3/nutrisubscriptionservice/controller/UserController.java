package project3.nutrisubscriptionservice.controller;


import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.ChangePasswordDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.dto.UserProfileDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;
import project3.nutrisubscriptionservice.service.UserProfileService;
import project3.nutrisubscriptionservice.service.UserService;

@RestController
@Getter
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;


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
                    .password(user.getPassword())
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


//회원정보 확인
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDTO> profilePage(@PathVariable Long userId, Model model) {
        UserProfileDTO userProfileDTO = userProfileService.getUserProfile(userId);
        model.addAttribute("user", userProfileDTO);
        return ResponseEntity.ok(userProfileDTO);
    }

    @PostMapping("/profile/update")
    public String updateProfile(UserProfileDTO userProfileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//
//        // 사용자의 이메일을 기반으로 사용자의 ID를 찾아옵니다.
//        UserEntity userEntity = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Long userId = (Long) authentication.getPrincipal();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//        Long userId = userEntity.getId();;

        // 사용자의 ID와 프로필 DTO를 사용하여 프로필을 업데이트합니다.
        userProfileService.updateUserProfile(userId, userProfileDTO);

        return "redirect:/user/profile";
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