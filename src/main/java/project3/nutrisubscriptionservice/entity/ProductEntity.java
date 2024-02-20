package project3.nutrisubscriptionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private int productId;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="category_id")
    private CategoryEntity category;

    @Column(name="p_name", length = 50, nullable = false)
    private String pName;

    @Column(name="p_price", nullable = false)
    private int pPrice;

    @Column(name="p_info", length = 100, nullable = false)
    private String pInfo;
}