package project3.nutrisubscriptionservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartDTO {
    private long cartId;
    private List<ProductDTO> products;
    private long userId;
}
