package com.jshop.product_category.batch;

import com.jshop.infra.util.ValidationUtil;
import com.jshop.product_category.converter.ImportProductCategoryCmsConverter;
import com.jshop.product_category.domain.ProductCategory;
import com.jshop.product_category.pojo.vo.ImportProductCategoryCmsVoReq;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportProductCategoryProcessor implements ItemProcessor<ImportProductCategoryCmsVoReq, ProductCategory> {
    private final ImportProductCategoryCmsConverter importProductCategoryCmsConverter;

    @Override
    public ProductCategory process(ImportProductCategoryCmsVoReq item) {
        ValidationUtil.validate(item);
        return importProductCategoryCmsConverter.toProductCategory(item);
    }
}
