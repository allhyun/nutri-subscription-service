package project3.nutrisubscriptionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project3.nutrisubscriptionservice.service.FormService;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;
    @GetMapping("")
    public String getForm(@AuthenticationPrincipal String id) {

//        String form = formService.getForm(id);

        // 가져온 양식을 반환합니다.
        return "GET /form" + id ;
    }
}