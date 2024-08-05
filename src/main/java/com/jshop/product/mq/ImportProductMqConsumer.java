package com.jshop.product.mq;

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
public class ImportProductMqConsumer {
    private final JobLauncher jobLauncher;
    private final Job importProductJob;

    @RabbitListener(queues = ImportProductMqConfig.IMPORT_PRODUCT_QUEUE, ackMode = "MANUAL")
    public void importProduct(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("csvFilePath", new String(message.getBody()))
                    .toJobParameters();

            jobLauncher.run(importProductJob, jobParameters);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
