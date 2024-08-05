package com.jshop.product_category.service;

import com.jshop.product_category.mq.ImportProductCategoryMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryCmsService {

    @Value("${project.volumn-path}")
    private String volumnPath;
    private final RabbitTemplate rabbitTemplate;

    public void uploadFile(MultipartFile file) throws IOException {
        String directoryPath = volumnPath + File.separator + "etl" + File.separator + "product_category";
        String filename = UUID.randomUUID() + file.getOriginalFilename();
        String filePath = directoryPath + File.separator + filename;

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        file.transferTo(new File(filePath));

        rabbitTemplate.convertAndSend(ImportProductCategoryMqConfig.IMPORT_PRODUCT_CATEGORY_QUEUE, filePath);
    }
}
