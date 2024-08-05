package com.jshop.product_category.mq;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductCategoryMqConsumer {
    private final JobLauncher jobLauncher;
    private final Job importProductCategoryJob;

    @RabbitListener(queues = ImportProductCategoryMqConfig.IMPORT_PRODUCT_CATEGORY_QUEUE, ackMode = "MANUAL")
    public void importProductCategory(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("csvFilePath", new String(message.getBody()))
                    .toJobParameters();

            jobLauncher.run(importProductCategoryJob, jobParameters);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            log.error("Import Product Error", e);
        }
    }
}
