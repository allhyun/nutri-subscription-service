package project3.nutrisubscriptionservice.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.ChangePasswordDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.dto.UserProfileDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.Optional;

@Service
@Setter
public class UserProfileService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //비밀번호 변경 메서드
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 현재 비밀번호가 맞는지 확인하는 로직 구현
        if (!userEntity.getPassword().equals(changePasswordDTO.getCurrentPassword())) {
            throw new RuntimeException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새로운 비밀번호로 변경
        userEntity.setPassword(changePasswordDTO.getNewPassword());
        // 변경된 비밀번호 정보를 설정
//        changePasswordDTO.setUpdatedPassword(changePasswordDTO.getNewPassword());


        userRepository.save(userEntity);
    }



    //회원정보 확인


     //스프링이 로그인 요청을 가로챌 때, username과 password 변수 2개를 가로채는데 password는 // 우리는 username이 DB에 있는지만 확인해주면 된다.

    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(userEmail);
        if (userEntity == null) {
            // 사용자의 권한 정보를 가져와서 UserDetails 객체를 생성합니다.

            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail);

        }
        return User.withUsername(userEntity.getEmail())
                    .password(userEntity.getPassword())
                    .roles("user") // UserEntity에 권한 정보가 roles 필드로 저장되어 있다고 가정합니다.
                    .build();


    }

    // 회원정보 확인 메서드 추가
//    public UserProfileDTO getUserProfileById(Long userId) {
//        UserEntity userEntity = userRepository.findById(userId);
//        if (userEntity == null) {
//            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
//        }
//        return UserProfileDTO.fromUser(userEntity);
//    }




    public UserProfileDTO getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저정보를 찾을 수 없습니다.: " + userId));

        return UserProfileDTO.fromUser(userEntity);
    }

    public void updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userEntity.setEmail(userProfileDTO.getEmail());
        userEntity.setName(userProfileDTO.getName());
        userEntity.setPhone(userProfileDTO.getPhone());
        userEntity.setZipcode(userProfileDTO.getZipcode());
        userEntity.setAddress(userProfileDTO.getAddress());

        userRepository.save(userEntity);
    }



    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


}