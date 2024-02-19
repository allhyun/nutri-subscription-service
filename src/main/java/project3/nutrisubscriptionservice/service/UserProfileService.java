package project3.nutrisubscriptionservice.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.ChangePasswordDTO;
import project3.nutrisubscriptionservice.dto.UserProfileDTO;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;

@Service
@Setter
public class UserProfileService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


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

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}