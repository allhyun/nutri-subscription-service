package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.entity.FormEntity;
import project3.nutrisubscriptionservice.entity.ProductEntity;

import java.util.List;

// Entity를 통해 데이터 테이블이 생성이 되면,
// 받아온 정보를 데이터베이스(ex. MySQL, mariaDB)에 저장하고 조회하는 기능을 수행
@Repository
public interface FormRepository extends JpaRepository<FormEntity, Integer> {
}

