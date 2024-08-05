package com.jshop.product_category.controller;

import com.jshop.product_category.service.ProductCategoryCmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cms/product-category")
public class ProductCategoryCmsController {

    private final ProductCategoryCmsService productCategoryCmsService;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        productCategoryCmsService.uploadFile(file);
    }
}
