package com.jshop.product.domain;

import com.jshop.product_category.domain.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asin", nullable = false)
    private String asin;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "product_url", nullable = false)
    private String productUrl;

    @Column(name = "starts", nullable = false)
    private Double starts;

    @Column(name = "reviews", nullable = false)
    private Integer reviews;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "list_price", nullable = false)
    private Double listPrice;

    @JoinColumn(name = "product_category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    @Column(name = "is_best_seller", nullable = false)
    private Boolean isBestSeller;

    @Column(name = "bought_in_last_month", nullable = false)
    private Integer boughtInLastMonth;
}
