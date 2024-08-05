package com.jshop.product.controller;

import com.jshop.product.service.ProductCmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cms/product")
public class ProductCmsController {

    private final ProductCmsService productCmsService;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        productCmsService.uploadFile(file);
    }
}
