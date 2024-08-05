package com.jshop.product.batch;

import com.jshop.infra.util.ValidationUtil;
import com.jshop.product.pojo.vo.ImportProductCmsVoReq;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportProductProcessor implements ItemProcessor<ImportProductCmsVoReq, ImportProductCmsVoReq> {

    @Override
    public ImportProductCmsVoReq process(ImportProductCmsVoReq item) throws Exception {
        ValidationUtil.validate(item);
        return item;
    }
}
