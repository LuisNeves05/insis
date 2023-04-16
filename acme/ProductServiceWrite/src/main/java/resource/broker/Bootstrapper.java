package resource.broker;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import resource.service.ProductService;
import resource.service.command_bus.CreateProductCommand;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Component
public class Bootstrapper {

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private ProductService service;

    private boolean handleResponseSuccess = true;

    @Autowired
    public Bootstrapper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        int i = 1;
        do {
            String i_str = Integer.toString(i);
            requestAllProducts(i_str);
            i++;
        } while (handleResponseSuccess);
    }

    public void requestAllProducts(String id) {

        try {
            String correlationId = UUID.randomUUID().toString();
            String replyQueueName = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false).queueDeclare().getQueue();

            CountDownLatch latch = new CountDownLatch(1);

            MessageProperties properties = new MessageProperties();
            properties.setCorrelationId(correlationId);
            properties.setReplyTo(replyQueueName);
            properties.setHeader("id", id);
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.BOOTSTRAP_PRODUCT, SerializationUtils.serialize(properties));

            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
            container.setQueueNames(replyQueueName);
            container.setMessageListener((MessageListener) message -> {
                handleResponse(message.getBody());
                // Count down the latch to indicate that the message has been received
                latch.countDown();
            });
            container.start();

            if (latch.await(5, TimeUnit.SECONDS)) {
                container.stop();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleResponse(byte[] messageBytes) {
        boolean response = false;
        try {
            CreateProductCommand createProductCommand = (CreateProductCommand) SerializationUtils.deserialize(messageBytes);
            switch (createProductCommand.getKeyEvent()) {
                case RabbitMQConfig.PRODUCT_CREATE_RK -> service.create(createProductCommand);
                case RabbitMQConfig.PRODUCT_UPDATE_RK -> service.updateBySku(createProductCommand);
                case RabbitMQConfig.PRODUCT_DELETE_RK -> service.deleteBySku(createProductCommand);
            }
            response = true;
        } catch (ClassCastException ignore) {
        }
        handleResponseSuccess = response;
    }

    //    public boolean requestProduct(Channel channel, String message) throws IOException {
//        final CompletableFuture<Boolean> value = new CompletableFuture<>();
//
//        final String corrId = UUID.randomUUID().toString();
//        String replyQueueName = channel.queueDeclare().getQueue();
//        AMQP.BasicProperties props = new AMQP.BasicProperties
//                .Builder()
//                .correlationId(corrId)
//                .replyTo(replyQueueName)
//                .build();
//
//        String requestQueueName = "bootstrap_product";
//        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
//
//        final CompletableFuture<String> response = new CompletableFuture<>();
//
//        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
//            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
//                response.complete(new String(delivery.getBody(), "UTF-8"));
//                if (delivery.getBody().length != 0) {
//                    value.complete(true);
//
//                    CreateProductCommand createProductCommand = (CreateProductCommand) SerializationUtils.deserialize(delivery.getBody());
//
//                    switch (createProductCommand.getKeyEvent()) {
//                        case RabbitMQConfig.PRODUCT_CREATE_RK -> service.create(createProductCommand);
//                        case RabbitMQConfig.PRODUCT_UPDATE_RK -> service.updateBySku(createProductCommand);
//                        case RabbitMQConfig.PRODUCT_DELETE_RK -> service.deleteBySku(createProductCommand);
//                    }
//
//                } else {
//                    value.complete(false);
//                }
//            }
//        }, consumerTag -> {
//        });
//
//        channel.basicCancel(ctag);
//
//        return value.join();
//    }

}

