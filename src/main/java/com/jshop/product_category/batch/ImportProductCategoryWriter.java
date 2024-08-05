package com.jshop.product_category.batch;

import com.google.common.collect.Maps;
import com.jshop.product_category.domain.ProductCategory;
import com.jshop.product_category.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ImportProductCategoryWriter implements ItemWriter<ProductCategory> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void write(Chunk<? extends ProductCategory> chunk) {
        String sql = "INSERT INTO product_category (id, name) VALUES (:id, :name)";
        List<ProductCategory> items = (List<ProductCategory>) chunk.getItems();

        Map<String, Object>[] batchValues = new Map[items.size()];
        for (int i = 0; i < items.size(); i++) {
            ProductCategory productCategory = items.get(i);
            batchValues[i] = Maps.newHashMap();
            batchValues[i].put("id", productCategory.getId());
            batchValues[i].put("name", productCategory.getName());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
    }
}
