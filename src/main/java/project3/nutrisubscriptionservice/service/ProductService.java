package project3.nutrisubscriptionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.dto.CategoryDTO;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.entity.CategoryEntity;
import project3.nutrisubscriptionservice.entity.ProductEntity;
import project3.nutrisubscriptionservice.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    // 카테고리 DTO 생성
    private CategoryDTO getCategoryDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .category_id(categoryEntity.getCategoryId())
                .category_name(categoryEntity.getCategoryName())
                .build();
    }

    // 모든 제품 조회
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();

        return productEntities.stream()
                .map(productEntity -> {
                    CategoryDTO categoryDTO = getCategoryDTO(productEntity.getCategory());

                    return ProductDTO.builder()
                            .product_id(productEntity.getProductId())
                            .category_id(categoryDTO.getCategory_id())
                            .p_price(productEntity.getPPrice())
                            .p_name(productEntity.getPName())
                            .p_info(productEntity.getPInfo())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 특정 제품 조회
    public ProductDTO getProductById(Long productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("해당 제품을 찾을 수 없습니다."));

        CategoryDTO categoryDTO = getCategoryDTO(productEntity.getCategory());

        return ProductDTO.builder()
                .product_id(productEntity.getProductId())
                .category_id(categoryDTO.getCategory_id())
                .p_price(productEntity.getPPrice())
                .p_name(productEntity.getPName())
                .p_info(productEntity.getPInfo())
                .build();
    }
}