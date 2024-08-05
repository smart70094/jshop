package com.jshop.product.batch;

import com.jshop.product.pojo.vo.ImportProductCmsVoReq;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ImportProductBatchConfig {
    private final ImportProductProcessor importProductProcessor;
    private final ImportProductWriter importProductWriter;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public FlatFileItemReader<ImportProductCmsVoReq> importProductReader(@Value("#{jobParameters['csvFilePath']}") String csvFilePath) {
        return new FlatFileItemReaderBuilder<ImportProductCmsVoReq>()
                .name("import-product-reader")
                .linesToSkip(1)
                .resource(new FileSystemResource(csvFilePath))
                .delimited()
                .names("asin", "title", "imgUrl", "productUrl", "stars", "reviews", "price", "listPrice", "productCategoryId", "isBestSeller", "boughtInLastMonth")
                .targetType(ImportProductCmsVoReq.class)
                .build();
    }

    @Bean
    public TaskExecutor importProductTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    @Bean
    public Step importProductStep() {
        return new StepBuilder("import-product-step", jobRepository)
                .<ImportProductCmsVoReq, ImportProductCmsVoReq>chunk(100, platformTransactionManager)
                .reader(importProductReader(null))
                .processor(importProductProcessor)
                .writer(importProductWriter)
                .taskExecutor(importProductTaskExecutor())
                .build();
    }

    @Bean
    public Job importProductJob() {
        return new JobBuilder("import-product-job", jobRepository)
                .start(importProductStep())
                .build();
    }
}
