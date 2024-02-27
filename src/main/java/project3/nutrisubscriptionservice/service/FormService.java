package project3.nutrisubscriptionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import project3.nutrisubscriptionservice.dto.FormDTO;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.entity.FormEntity;
import project3.nutrisubscriptionservice.entity.ProductEntity;
import project3.nutrisubscriptionservice.entity.UserEntity;
import project3.nutrisubscriptionservice.repository.FormRepository;
import project3.nutrisubscriptionservice.repository.ProductRepository;
import project3.nutrisubscriptionservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public FormEntity saveForm(FormDTO formDTO) {
        // formDTO에서 userId를 가져와 UserEntity 찾기
        UserEntity userEntity = userRepository.findById(formDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        FormEntity formEntity = FormEntity.builder()
                .resultId(formDTO.getResultId())
                .user(userEntity)
                .height(formDTO.getHeight())
                .weight(formDTO.getWeight())
                .gender(formDTO.getGender())
                .answer1(formDTO.getAnswer1())
                .answer2(formDTO.getAnswer2())
                .answer3(formDTO.getAnswer3())
                .answer4(formDTO.getAnswer4())
                .answer5(formDTO.getAnswer5())
                .answer6(formDTO.getAnswer6())
                .answer7(formDTO.getAnswer7())
                .answer8(formDTO.getAnswer8())
                .build();

        return formRepository.save(formEntity);
    }

    // 추천
    @Transactional(readOnly = true)
    public List<ProductDTO> recommendProducts(long resultId) {
        FormEntity formEntity = formRepository.findByResultId(resultId);
        FormDTO formDTO = convertToDTO(formEntity);

        List<ProductDTO> recommendedProducts = new ArrayList<>();


        // 설문 결과를 분석하여 추천할 카테고리 ID와 제품 이름을 결정
        if (formDTO.getAnswer1() + formDTO.getAnswer2() >= 10) {
            recommendedProducts.addAll(getProductDTOs("영양제1"));
        } else if (formDTO.getAnswer1() + formDTO.getAnswer2() >= 6) {
            recommendedProducts.addAll(getProductDTOs("영양제2"));
        }

        if (formDTO.getAnswer3() + formDTO.getAnswer4() >= 10) {
            recommendedProducts.addAll(getProductDTOs("영양제3"));
        } else if (formDTO.getAnswer3() + formDTO.getAnswer4() >= 6) {
            recommendedProducts.addAll(getProductDTOs("영양제4"));
        }

        if (formDTO.getAnswer5() + formDTO.getAnswer6() >= 10) {
            recommendedProducts.addAll(getProductDTOs("영양제5"));
        } else if (formDTO.getAnswer5() + formDTO.getAnswer6() >= 6) {
            recommendedProducts.addAll(getProductDTOs("영양제6"));
        }

        if (formDTO.getAnswer7() + formDTO.getAnswer8() >= 10) {
            recommendedProducts.addAll(getProductDTOs("영양제7"));
        } else if (formDTO.getAnswer7() + formDTO.getAnswer8() >= 6) {
            recommendedProducts.addAll(getProductDTOs("영양제8"));
        }

        return recommendedProducts;
    }

    private FormDTO convertToDTO(FormEntity formEntity) {
        return FormDTO.builder()
                .resultId(formEntity.getResultId())
                .userId(formEntity.getUser().getId())
                .height(formEntity.getHeight())
                .weight(formEntity.getWeight())
                .gender(formEntity.getGender())
                .answer1(formEntity.getAnswer1())
                .answer2(formEntity.getAnswer2())
                .answer3(formEntity.getAnswer3())
                .answer4(formEntity.getAnswer4())
                .answer5(formEntity.getAnswer5())
                .answer6(formEntity.getAnswer6())
                .answer7(formEntity.getAnswer7())
                .answer8(formEntity.getAnswer8())
                .build();
    }

    private List<ProductDTO> getProductDTOs(String pName) {
        List<ProductEntity> productList = productRepository.findBypName(pName);

        return productList.stream()
                .map(product -> ProductDTO.builder()
                        .product_id(product.getProductId())
                        .category_id(product.getCategory().getCategoryId())
                        .p_price(product.getPPrice())
                        .p_name(product.getPName())
                        .p_info(product.getPInfo())
                        .build())
                .collect(Collectors.toList());
    }
}