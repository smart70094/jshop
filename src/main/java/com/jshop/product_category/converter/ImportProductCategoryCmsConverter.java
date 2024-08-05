package com.jshop.product_category.converter;

import com.jshop.product_category.domain.ProductCategory;
import com.jshop.product_category.pojo.vo.ImportProductCategoryCmsVoReq;
import org.springframework.stereotype.Component;

@Component
public class ImportProductCategoryCmsConverter {

    public ProductCategory toProductCategory(ImportProductCategoryCmsVoReq request) {
        return ProductCategory.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
    }
}
