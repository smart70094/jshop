package com.jshop.product_category.batch;

import com.jshop.product_category.domain.ProductCategory;
import com.jshop.product_category.pojo.vo.ImportProductCategoryCmsVoReq;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ImportProductCategoryBatchConfig {
    private final ImportProductCategoryProcessor importProductCategoryProcessor;
    private final ImportProductCategoryWriter importProductCategoryWriter;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @StepScope
    public FlatFileItemReader<ImportProductCategoryCmsVoReq> importProductCategoryReader(@Value("#{jobParameters['csvFilePath']}") String csvFilePath) {
        return new FlatFileItemReaderBuilder<ImportProductCategoryCmsVoReq>()
                .name("import-product-category-reader")
                .linesToSkip(1)
                .resource(new FileSystemResource(csvFilePath))
                .delimited()
                .names("id", "name")
                .targetType(ImportProductCategoryCmsVoReq.class)
                .build();
    }

    @Bean
    public Step importProductCategoryStep() {
        return new StepBuilder("import-product-category-step", jobRepository)
                .<ImportProductCategoryCmsVoReq, ProductCategory>chunk(100, transactionManager)
                .reader(importProductCategoryReader(null))
                .processor(importProductCategoryProcessor)
                .writer(importProductCategoryWriter)
                .build();
    }

    @Bean
    public Job importProductCategoryJob() {
        return new JobBuilder("import-product-category-job", jobRepository)
                .start(importProductCategoryStep())
                .build();
    }
}
