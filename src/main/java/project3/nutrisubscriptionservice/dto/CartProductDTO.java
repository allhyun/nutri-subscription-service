package project3.nutrisubscriptionservice.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartProductDTO {
    private long cartId;
    private long productId;
    private int quantity;
}
