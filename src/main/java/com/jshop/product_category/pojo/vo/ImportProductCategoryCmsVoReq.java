package com.jshop.product_category.pojo.vo;

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
public class ImportProductCategoryCmsVoReq {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
