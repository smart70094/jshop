package com.jshop.product.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.jshop.product.domain.Product;
import com.jshop.product.pojo.vo.ImportProductCmsVoReq;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ImportProductWriter implements ItemWriter<ImportProductCmsVoReq> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void write(Chunk<? extends ImportProductCmsVoReq> chunk) throws Exception {
        String sql =
                "INSERT INTO product (asin, bought_in_last_month, img_url, is_best_seller, list_price, price, product_url, reviews, starts, product_category_id)\n" +
                "VALUES (\n" +
                ":asin,:boughtInLastMonth,:imgUrl,:isBestSeller,\n" +
                ":listPrice,:price,:productUrl,\n" +
                ":reviews,:starts,:productCategoryId\n" +
                ");";

        List<ImportProductCmsVoReq> items = (List<ImportProductCmsVoReq>) chunk.getItems();

        Map<String, Object>[] batchValues = new Map[items.size()];
        for (int i = 0; i < items.size(); i++) {
            ImportProductCmsVoReq request = items.get(i);
            batchValues[i] = objectMapper.convertValue(request, Map.class);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
    }
}
