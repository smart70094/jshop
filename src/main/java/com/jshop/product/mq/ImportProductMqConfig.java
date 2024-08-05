package com.jshop.product.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportProductMqConfig {

    public static final String IMPORT_PRODUCT_QUEUE = "import-product-queue";
    public static final String DEAD_IMPORT_PRODUCT_QUEUE = "dead-import-product-queue";
    private static final String IMPORT_PRODUCT_EXCHANGE = "import-product-exchange";

    @Bean
    public Queue importProductQueue() {
        return QueueBuilder.durable(IMPORT_PRODUCT_QUEUE)
                .withArgument("x-dead-letter-exchange", IMPORT_PRODUCT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_IMPORT_PRODUCT_QUEUE)
                .withArgument("x-message-ttl", 1000 * 60 * 15)
                .build();
    }

    @Bean
    public DirectExchange importProductExchange() {
        return new DirectExchange(IMPORT_PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding importProductBinding(Queue importProductQueue, DirectExchange importProductExchange) {
        return BindingBuilder.bind(importProductQueue).to(importProductExchange).with(IMPORT_PRODUCT_QUEUE);
    }

    @Bean
    public Queue deadImportProductQueue() {
        return new Queue(DEAD_IMPORT_PRODUCT_QUEUE);
    }

    @Bean
    public Binding deadImportProductBinding(Queue deadImportProductQueue, DirectExchange importProductExchange) {
        return BindingBuilder.bind(deadImportProductQueue).to(importProductExchange).with(DEAD_IMPORT_PRODUCT_QUEUE);
    }
}
