package project3.nutrisubscriptionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project3.nutrisubscriptionservice.dto.FormDTO;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.entity.FormEntity;
import project3.nutrisubscriptionservice.service.FormService;
import project3.nutrisubscriptionservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/form")
@Slf4j
public class FormController {
    @Autowired
    private FormService formService;

    @Autowired
    private ProductService productService;

    // 설문 결과 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/submit")
    public ResponseEntity<?> saveForm(@RequestBody FormDTO formDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            FormEntity savedForm = formService.saveForm(formDTO);
            return new ResponseEntity<>(savedForm, HttpStatus.CREATED);
        } catch (Exception e) {
            // 오류 메시지 로그로 출력
            e.printStackTrace();
            return new ResponseEntity<>("설문 결과를 저장하는 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 추천 제품 조회
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recommendations/{resultId}")
    public ResponseEntity<?> recommendProducts(@PathVariable long resultId) {
        try {
            List<ProductDTO> recommendedProducts = formService.recommendProducts(resultId);
            return new ResponseEntity<>(recommendedProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("제품을 추천하는 과정에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
