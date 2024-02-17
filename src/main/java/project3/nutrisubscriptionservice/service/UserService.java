package project3.nutrisubscriptionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.UserRepository;

@Service

public class UserService {
    @Autowired
    UserRepository userRepository;

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
}
