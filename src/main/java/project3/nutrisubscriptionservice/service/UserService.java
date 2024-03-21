package project3.nutrisubscriptionservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    //암호화 객체 생성
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity create(UserEntity userEntity){
        if(userEntity==null){
            throw new RuntimeException("entity null");
        }
        String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        return userRepository.save(userEntity);
    }

    public UserEntity login(String email,String password){
        UserEntity searchUser = userRepository.findByEmail(email);
        if(searchUser != null && passwordEncoder.matches(password, searchUser.getPassword()));
        return searchUser;
    }

    //사용자 인증
    public static Optional<String> getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            log.debug("Security Context에 인증정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        }else if(authentication.getPrincipal() instanceof String){
            username = (String)authentication.getPrincipal();
        }
        return Optional.ofNullable(username);
    }



    public Optional<UserEntity> getMyUserWithAuthorities() {
        return getCurrentUsername().flatMap(id -> {
            try {
                Long userId = Long.parseLong(id); // id를 Long으로 변환
                log.info("Current User ID: {}", userId);
                return userRepository.findById(userId); // 변환된 userId로 사용자 조회
            } catch (NumberFormatException e) {
                log.error("ID 형식 변환 오류: {}", e.getMessage());
                return Optional.empty(); // 형식 변환 오류 발생 시 빈 Optional 반환
            }
        });
    }

}
