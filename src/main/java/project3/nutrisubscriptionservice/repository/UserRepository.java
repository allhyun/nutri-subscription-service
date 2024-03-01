package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity , Long> {


    UserEntity findByEmail(String email);

    Boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);
//    @Query("select u from UserEntity u where (u.name = :name or :name = '')")
//    List<UserEntity> searchByWord(String word);

}