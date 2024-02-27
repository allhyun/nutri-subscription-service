package project3.nutrisubscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDTO {
    private long category_id;
    private String category_name;
}
