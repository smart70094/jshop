package com.jshop.product.pojo.vo;

import com.jshop.product_category.domain.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportProductCmsVoReq {
    @NotBlank
    private String asin;

    @NotBlank
    private String title;

    @NotBlank
    private String imgUrl;

    @NotBlank
    private String productUrl;

    @NotNull
    private Double starts;

    @NotNull
    private Integer reviews;

    @NotNull
    private Double price;

    @NotNull
    private Double listPrice;

    @NotNull
    private Long productCategoryId;

    @NotNull
    private Boolean isBestSeller;

    @NotNull
    private Integer boughtInLastMonth;
}
