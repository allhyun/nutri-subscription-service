package project3.nutrisubscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter//다현
public class ProductDTO {
    private long product_id;
    private long category_id;
    private int p_price;
    private String p_name;
    private String p_info;
}
