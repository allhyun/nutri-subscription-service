package project3.nutrisubscriptionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project3.nutrisubscriptionservice.entity.ProductEntity;
import project3.nutrisubscriptionservice.service.ProductService;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/recommend-products")
    public ResponseEntity<List<ProductEntity>> recommendProducts(
            @RequestParam("answer1") int answer1,
            @RequestParam("answer2") int answer2,
            @RequestParam("answer3") int answer3,
            @RequestParam("answer4") int answer4,
            @RequestParam("answer5") int answer5,
            @RequestParam("answer6") int answer6,
            @RequestParam("answer7") int answer7,
            @RequestParam("answer8") int answer8
    ) {
        List<ProductEntity> recommendedProducts = productService.recommendProducts(
                answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8);
        return new ResponseEntity<>(recommendedProducts, HttpStatus.OK);
    }
}
