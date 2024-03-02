package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Embeddable
public class CartProductKey implements Serializable {
    @Column(name = "cart_id")
    private long cartId;

    @Column(name = "product_id")
    private long productId;
}
