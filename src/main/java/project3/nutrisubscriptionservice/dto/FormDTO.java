package project3.nutrisubscriptionservice.dto;

// DTO: 클라이언트와 서버 간의 데이터 전송을 위한 객체.
// 클라이언트 측: DTO로 설문조사 결과를 서버에 전달,
// 서버 측: DTO를 사용해 클라이언트로부터 전달된 설문조사 결과를 처리합니다.

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FormDTO {
    private long resultId;
    private long userId;
    private int height;
    private int weight;
    private String gender;
    private int answer1;
    private int answer2;
    private int answer3;
    private int answer4;
    private int answer5;
    private int answer6;
    private int answer7;
    private int answer8;
}
