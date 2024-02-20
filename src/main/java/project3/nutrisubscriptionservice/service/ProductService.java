package project3.nutrisubscriptionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project3.nutrisubscriptionservice.entity.ProductEntity;
import project3.nutrisubscriptionservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<ProductEntity> recommendProducts(int answer1, int answer2, int answer3, int answer4, int answer5, int answer6, int answer7, int answer8) {
        // 각 질문에 따른 점수를 계산
        int score1 = answer1 + answer2;
        int score2 = answer3 + answer4;
        int score3 = answer5 + answer6;
        int score4 = answer7 + answer8;

        List<ProductEntity> recommendedProducts = new ArrayList<>();

        if (score1 >= 10) {
            recommendedProducts.addAll(productRepository.findBypName("영양제1"));
        } else if (score1 >= 6) {
            recommendedProducts.addAll(productRepository.findBypName("영양제2"));
        }

        if (score2 >= 10) {
            recommendedProducts.addAll(productRepository.findBypName("영양제3"));
        } else if (score2 >= 6) {
            recommendedProducts.addAll(productRepository.findBypName("영양제4"));
        }

        if (score3 >= 10) {
            recommendedProducts.addAll(productRepository.findBypName("영양제5"));
        } else if (score3 >= 6) {
            recommendedProducts.addAll(productRepository.findBypName("영양제6"));
        }

        if (score4 >= 10) {
            recommendedProducts.addAll(productRepository.findBypName("영양제7"));
        } else if (score4 >= 6) {
            recommendedProducts.addAll(productRepository.findBypName("영양제8"));
        }

        return recommendedProducts;
    }
}
