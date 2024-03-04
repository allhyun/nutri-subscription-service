package project3.nutrisubscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CartDTO {
    private long cartId;
    private long userId;
    private List<CartProductDTO> cartProducts;
}
