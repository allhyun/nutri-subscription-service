package project3.nutrisubscriptionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.entity.ProductEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    List<ProductEntity> findByPName(String pName);

    // 제품 조회
//    List<ProductEntity> findByCategory_CategoryIdAndPName(long categoryId, String pName);

    List<ProductEntity> findBypName(String pName);

    Optional<ProductEntity> findByProductId(long productId);
}
