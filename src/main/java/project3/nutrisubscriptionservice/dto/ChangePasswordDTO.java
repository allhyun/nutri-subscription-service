package project3.nutrisubscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordDTO {

    private String currentPassword;
    private String newPassword;

    // 생성자, 게터, 세터 메서드 등을 정의합니다.

    public ChangePasswordDTO() {
        // 기본 생성자
    }

    public ChangePasswordDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

}
