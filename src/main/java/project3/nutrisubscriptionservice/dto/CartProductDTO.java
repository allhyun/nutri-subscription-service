package project3.nutrisubscriptionservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CartProductDTO {

    private long productId;
    private int quantity;
}
