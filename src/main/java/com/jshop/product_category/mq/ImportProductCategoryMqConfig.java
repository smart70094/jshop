package com.jshop.product_category.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportProductCategoryMqConfig {

    public static final String IMPORT_PRODUCT_CATEGORY_QUEUE = "import-product-category-queue";
    public static final String DEAD_IMPORT_PRODUCT_CATEGORY_QUEUE = "dead-import-product-category-queue";
    private static final String IMPORT_PRODUCT_CATEGORY_EXCHANGE = "import-product-category-exchange";

    @Bean
    public Queue importProductCategoryQueue() {
        return QueueBuilder.durable(IMPORT_PRODUCT_CATEGORY_QUEUE)
                .withArgument("x-dead-letter-exchange", IMPORT_PRODUCT_CATEGORY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_IMPORT_PRODUCT_CATEGORY_QUEUE)
                .withArgument("x-message-ttl", 1000 * 60 * 15)
                .build();
    }

    @Bean
    public DirectExchange importProductCategoryExchange() {
        return new DirectExchange(IMPORT_PRODUCT_CATEGORY_EXCHANGE);
    }

    @Bean
    public Binding importProductCategoryBinding(Queue importProductCategoryQueue, DirectExchange importProductCategoryExchange) {
        return BindingBuilder.bind(importProductCategoryQueue)
                .to(importProductCategoryExchange)
                .with(IMPORT_PRODUCT_CATEGORY_QUEUE);
    }

    @Bean
    public Queue deadImportProductCategoryQueue() {
        return new Queue(DEAD_IMPORT_PRODUCT_CATEGORY_QUEUE);
    }

    @Bean
    public Binding deadImportProductCategoryBinding(Queue deadImportProductCategoryQueue, DirectExchange importProductCategoryExchange) {
        return BindingBuilder.bind(deadImportProductCategoryQueue)
                .to(importProductCategoryExchange)
                .with(DEAD_IMPORT_PRODUCT_CATEGORY_QUEUE);
    }
}
