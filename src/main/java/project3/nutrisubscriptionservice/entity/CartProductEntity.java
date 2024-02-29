package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name="cart_product")
public class CartProductEntity {
    @EmbeddedId
    private CartProductKey cartProductId;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private int quantity;

}